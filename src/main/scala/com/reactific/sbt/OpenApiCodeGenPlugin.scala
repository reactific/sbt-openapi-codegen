/*
 * Copyright 2018-2018 Reactific Software LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.reactific.sbt

import java.io.File

import scala.util.Failure
import scala.util.Success
import scala.util.Try
import scala.util.matching.Regex

import sbt._
import sbt.Keys._
import _root_.io.swagger.codegen.SwaggerCodegen
import sbt.TaskKey
import sbt.internal.util.ManagedLogger
import sbt.plugins.JvmPlugin

object OpenApiCodeGenPlugin extends AutoPlugin {

  override def trigger: PluginTrigger = allRequirements
  override def requires: Plugins = JvmPlugin
  
  object autoImport {
  
    /**
     * This is used to execute one run of the code gener
     * @param codegenType The language to generate as either its simple
     *                    name or the class name in the classpath (required)
     * @param openApiSpec location of the OpenAPI spec, as URL or file
     *                    path (required)
     * @param outputDir where to write the generated files
     *                  (.../target/codegen by default)
     
     * @param configFile Path to json configuration file. File content should
     *                   be in a jso format {"optionKey":"optionValue",
     *                   "optionKey1":"optionValue1" ...} Supported options can
     *                   be different for each language. Run config-help -l
     *                   {lang} command for language specific config options.
     * @param templatesDir The path to the directory containing the moustache
     *                     templates to use for code generation. If 'None',
     *                     use the defaults
     * @param library library template (sub-template)
    // --api-package <api package>
    val apiPackage: SettingKey[String] =
      settingKey[String]("name of package for the api output")
  
    // --model-package <model package>
    val modelPackage: SettingKey[String] =
      settingKey[String]("name of package for the model output")
  
    // --invoker-package <invoker package>
    val invokerPackage: SettingKey[String] =
      settingKey[String]("name of root package for generated code")
  
    // --model-name-prefix
    val modelNamePrefix: SettingKey[String] =
      settingKey[String]("A prefix to prepend to each model name")
  
    // --model-name-suffix
    val modelNameSuffix: SettingKey[String] =
      settingKey[String]("A suffix to append to each model name")
  
    // -v --verbose
    val verbose: SettingKey[Boolean] = settingKey[Boolean](
      "Run the swagger-codegen-cli with verbose output option turned on"
    )
  
    // -s --skip-overwrite
    val skipOverwite: SettingKey[Boolean] = settingKey[Boolean](
      "specifies if the existing files should be " +
        "overwritten during the generation."
    )
  
    // --git-repo-id <git repo id>
    val gitRepoId: SettingKey[String] = settingKey[String](
      "Git repo ID, e.g. swagger-codegen."
    )
  
    // --git-user-id <git user id>
    val gitUserId: SettingKey[String] = settingKey[String](
      "Git user ID, e.g. swagger-api."
    )
  
    // --group-id <group id>
    val pomGroupId: SettingKey[String] = settingKey[String](
      "groupId in generated pom.xml\n  "
    )
  
    // --artifact-id <artifact id>
    val pomArtifactId: SettingKey[String] = settingKey[String](
      "artifactId in generated pom.xml"
    )
  
    // --artifact-version <artifact version>
    val pomArtifaceVersion: SettingKey[String] = settingKey[String](
      "artifact version in generated pom.xml"
    )
  
    // -a <authorization>, --auth <authorization>
    val httpAuthorization: SettingKey[String] = settingKey[String](
      """Adds authorization headers when fetching the swagger definitions
        | remotely. Pass in a URL-encoded string of name:header with a
        | comma separating multiple values
        |""".stripMargin
    )
  
    // --http-user-agent <http user agent>
    val httpUserAgent: SettingKey[String] = settingKey[String](
      "HTTP user agent, e.g. codegen_csharp_api_client, default to" +
        "'Swagger-Codegen/{packageVersion}}/{language}'"
    )
  
    // --release-note <release note>
    val releaseNotes: SettingKey[String] = settingKey[String](
      "Release notes, defaults to 'Minor update'."
    )
  
    // --remove-operation-id-prefix
    val removeOperationIdPrefix: SettingKey[String] = settingKey[String](
      "Remove prefix of operationId, e.g. config_getId => getId"
    )     */
    case class CodeGenExecConfig(
      // -l <language>, --lang <language>
      codegenType: String,
      // -c <configuration file>, --config <configuration file>
      openApiSpec: File,
      // -o <output directory>, --output <output directory>
      outputDir: Option[File],
      
      // -i <spec file>, --input-spec <spec file>
      configFile: Option[File] = None,
      
      // -t <template directory>, --template-dir <template directory>
      templatesDir: Option[File] = None,
  
      // --library <library>
      library: String = "",
      
      // --api-package <api package>
      apiPackage: String = "",
    
      // --model-package <model package>
      modelPackage: String = "",
    
      // --invoker-package <invoker package>
      invokerPackage: String = "",
    
      // --model-name-prefix
      modelNamePrefix: String = "",
    
      // --model-name-suffix
      modelNameSuffix: String = "",
    
      // -v --verbose
      verbose: Boolean = false,
    
      // -s --skip-overwrite
      skipOverwite: Boolean = false,
    
      // --git-repo-id <git repo id>
      gitRepoId: String = "",
    
      // --git-user-id <git user id>
      gitUserId: String = "",
    
      // --group-id <group id>
      pomGroupId: String = "",
    
      // --artifact-id <artifact id>
      pomArtifactId: String = "",
    
      // --artifact-version <artifact version>
      pomArtifactVersion: String = "",
    
      // -a <authorization>, --auth <authorization>
      httpAuthorization: String = "",
    
      // --http-user-agent <http user agent>
      httpUserAgent: String = "",
    
      // --release-note <release note>
      releaseNotes: String = "Minor update",
    
      // --remove-operation-id-prefix
      removeOperationIdPrefix: String = ""
    )

    val generateWholeProject: SettingKey[Boolean] = settingKey[Boolean](
      "Controls whether an entire separate project (true) is generated for" +
        "separate compilation or the generated sources and resources are " +
        "added to the current build (false). The default is false."
    )

    // -l <language>, --lang <language>
    val codegenType: SettingKey[String] = settingKey[String](
      "The language to generate as either its simple name or the " +
        "class name in the classpath (required)"
    )
  
    // -c <configuration file>, --config <configuration file>
    val codegenConfigFile: SettingKey[File] = settingKey[File](
      """Path to json configuration file. File content should be in a json
        | format {"optionKey":"optionValue", "optionKey1":"optionValue1" ...}
        | Supported options can be different for each language. Run
        | config-help -l {lang} command for language specific config options.
        |""".stripMargin
    )

    // -i <spec file>, --input-spec <spec file>
    val openApiSpec: SettingKey[File] = settingKey[File](
      "location of the OpenAPI spec, as URL or file path (required)"
    )

    // -t <template directory>, --template-dir <template directory>
    val templatesDir: SettingKey[Option[File]] = settingKey[Option[File]](
      "The path to the directory containing the moustache templates to use" +
        "for code generation. If 'None', use the defaults"
    )

    // --library <library>
    val library: SettingKey[String] = settingKey[String](
      "library template (sub-template)"
    )

    // -o <output directory>, --output <output directory>
    val outputDir: SettingKey[Option[File]] = settingKey[Option[File]](
      "where to write the generated files (current dir by default)"
    )

    // --api-package <api package>
    val apiPackage: SettingKey[String] =
      settingKey[String]("name of package for the api output")

    // --model-package <model package>
    val modelPackage: SettingKey[String] =
      settingKey[String]("name of package for the model output")
  
    // --invoker-package <invoker package>
    val invokerPackage: SettingKey[String] =
      settingKey[String]("name of root package for generated code")

    // --model-name-prefix
    val modelNamePrefix: SettingKey[String] =
      settingKey[String]("A prefix to prepend to each model name")

    // --model-name-suffix
    val modelNameSuffix: SettingKey[String] =
      settingKey[String]("A suffix to append to each model name")

    // -v --verbose
    val verbose: SettingKey[Boolean] = settingKey[Boolean](
      "Run the swagger-codegen-cli with verbose output option turned on"
    )

    // -s --skip-overwrite
    val skipOverwite: SettingKey[Boolean] = settingKey[Boolean](
      "specifies if the existing files should be " +
        "overwritten during the generation."
    )
  
    // --git-repo-id <git repo id>
    val gitRepoId: SettingKey[String] = settingKey[String](
      "Git repo ID, e.g. swagger-codegen."
    )
  
    // --git-user-id <git user id>
    val gitUserId: SettingKey[String] = settingKey[String](
      "Git user ID, e.g. swagger-api."
    )

    // --group-id <group id>
    val pomGroupId: SettingKey[String] = settingKey[String](
      "groupId in generated pom.xml\n  "
    )
    
    // --artifact-id <artifact id>
    val pomArtifactId: SettingKey[String] = settingKey[String](
      "artifactId in generated pom.xml"
    )
    
    // --artifact-version <artifact version>
    val pomArtifaceVersion: SettingKey[String] = settingKey[String](
      "artifact version in generated pom.xml"
    )
  
    // -a <authorization>, --auth <authorization>
    val httpAuthorization: SettingKey[String] = settingKey[String](
      """Adds authorization headers when fetching the swagger definitions
        | remotely. Pass in a URL-encoded string of name:header with a
        | comma separating multiple values
        |""".stripMargin
    )

    // --http-user-agent <http user agent>
    val httpUserAgent: SettingKey[String] = settingKey[String](
      "HTTP user agent, e.g. codegen_csharp_api_client, default to" +
      "'Swagger-Codegen/{packageVersion}}/{language}'"
    )
  
    // --release-note <release note>
    val releaseNotes: SettingKey[String] = settingKey[String](
      "Release notes, defaults to 'Minor update'."
    )
  
    // --remove-operation-id-prefix
    val removeOperationIdPrefix: SettingKey[String] = settingKey[String](
      "Remove prefix of operationId, e.g. config_getId => getId"
    )
  
    /*

--additional-properties <additional properties>
  sets additional properties that can be referenced by the mustache
  templates in the format of name=value,name=value. You can also have
  multiple occurrences of this option.

-D <system properties>
  sets specified system properties in the format of
  name=value,name=value (or multiple options, each with name=value)

--ignore-file-override <ignore file override location>
  Specifies an override location for the .swagger-codegen-ignore file.
  Most useful on initial generation.

--import-mappings <import mappings>
  specifies mappings between a given class and the import that should
  be used for that class in the format of type=import,type=import. You
  can also have multiple occurrences of this option.

--instantiation-types <instantiation types>
  sets instantiation type mappings in the format of
  type=instantiatedType,type=instantiatedType.For example (in Java):
  array=ArrayList,map=HashMap. In other words array types will get
  instantiated as ArrayList in generated code. You can also have
  multiple occurrences of this option.

--language-specific-primitives <language specific primitives>
  specifies additional language specific primitive types in the format
  of type1,type2,type3,type3. For example:
  String,boolean,Boolean,Double. You can also have multiple
  occurrences of this option.

--reserved-words-mappings <reserved word mappings>
  specifies how a reserved name should be escaped to. Otherwise, the
  default _<name> is used. For example id=identifier. You can also
  have multiple occurrences of this option.

--type-mappings <type mappings>
  sets mappings between swagger spec types and generated code types in
  the format of swaggerType=generatedType,swaggerType=generatedType.
  For example: array=List,map=Map,string=String. You can also have
  multiple occurrences of this option.


*/
  
  
    val runSwaggerCodegenTask: TaskKey[Seq[File]] = TaskKey[Seq[File]](
     "run-swagger-codegen",
      "Task to generate code with swagger-codegen"
    )

    val collectSourceFilesTask: TaskKey[Seq[File]] = TaskKey[Seq[File]](
      "collect-source-files",
      "A task to generate just the source files from the code generate by " +
        "swagger-codegen"
    )

    val collectResourceFilesTask: TaskKey[Seq[File]] = TaskKey[Seq[File]](
      "collect-resource-files",
      "A task to generate just the resource files from the code generate by " +
        "swagger-codegen"
    )

  }

  import autoImport._

  override lazy val globalSettings = Seq()

  private val firstPublishedYear = 2018
  
  override lazy val buildSettings = Seq(
    organization := "com.reactific",
    organizationHomepage := Some(url("https://www.reactific.com/")),
    organizationName := "Reactific Software LLC",
    scalaVersion := "2.12.4",
    startYear := Some(firstPublishedYear)
  )

  override lazy val projectSettings = Seq(
    runSwaggerCodegenTask in Compile := {
      val outputRoot: File = outputDir.value match {
        case Some(dir) =>
          dir
        case None =>
          (sbt.Keys.managedSourceDirectories in Compile).value.head / "openapi"
      }
      val langToUse: String = languageToClassName(codegenType.value)
      runSwaggerCodegen(
        langToUse,
        templatesDir.value,
        openApiSpec.value,
        outputRoot,
        apiPackage.value,
        modelPackage.value,
        invokerPackage.value,
        generateWholeProject.value,
        verbose.value,
        skipOverwite.value,
        streams.value.log
      )
    }, collectSourceFilesTask in Compile := {
      (runSwaggerCodegenTask in Compile).value
        .filter(_.getAbsolutePath.contains("/main/scala/"))
    }, collectResourceFilesTask in Compile := {
      (runSwaggerCodegenTask in Compile).value
        .filter(_.getAbsolutePath.contains("/main/resources/"))
    }, generateWholeProject := false,
      sourceGenerators in Compile += (collectSourceFilesTask in Compile),
      resourceGenerators in Compile += (collectResourceFilesTask in Compile),
      codegenType := "scala-lagom-server",
      codegenConfigFile := baseDirectory.value / "codegenConfig.json",
      openApiSpec := file(""),
      templatesDir := None,
      verbose := true,
      skipOverwite := true,
      outputDir := None,
      apiPackage := "com.example.api",
      modelPackage := "com.example.model",
      invokerPackage := "com.example.invoker",
      modelNamePrefix := "",
      modelNameSuffix := "",
      libraryDependencies ++= Seq(
        "io.swagger" % "swagger-codegen-cli" % "2.3.1",
        "io.swagger" % "swagger-codegen-cli" % "2.3.1"
      )
    )

  // scalastyle:off
  def languageToClassName(lang: String): String = {
    import _root_.io.swagger.codegen.languages._
    lang match {
      case "ada" ⇒ classOf[AdaCodegen].getName
      case "ada-server" ⇒ classOf[AdaServerCodegen].getName
      case "akka-scala"          => classOf[AkkaScalaClientCodegen].getName
      case "android"             => classOf[AndroidClientCodegen].getName
      case "apache2"             => classOf[Apache2ConfigCodegen].getName
      case "apex"                => classOf[ApexClientCodegen].getName
      case "aspnetcore"          => classOf[AspNetCoreServerCodegen].getName
      case "bash"                => classOf[BashClientCodegen].getName
      case "csharp"              => classOf[CSharpClientCodegen].getName
      case "clojure"             => classOf[ClojureClientCodegen].getName
      case "cwiki"               => classOf[ConfluenceWikiGenerator].getName
      case "cpprest"             => classOf[CppRestClientCodegen].getName
      case "csharp-dotnet2"      => classOf[CsharpDotNet2ClientCodegen].getName
      case "dart"                => classOf[DartClientCodegen].getName
      case "dynamic-html"        => classOf[JavascriptClientCodegen].getName
      case "elixir"              => classOf[ElixirClientCodegen].getName
      case "elm"                 => classOf[ElmClientCodegen].getName
      case "eiffel"              => classOf[EiffelClientCodegen].getName
      case "erlang-client"       => classOf[ErlangClientCodegen].getName
      case "erlang-server"       => classOf[ErlangServerCodegen].getName
      case "finch"               => classOf[FinchServerCodegen].getName
      case "flash"               => classOf[FlashClientCodegen].getName
      case "python-flask"        => classOf[PythonClientCodegen].getName
      case "go"                  => classOf[GoClientCodegen].getName
      case "go-server"           => classOf[GoServerCodegen].getName
      case "groovy"              => classOf[GroovyClientCodegen].getName
      case "haskell-http-client" => classOf[HaskellHttpClientCodegen].getName
      case "haskell"             => classOf[HaskellServantCodegen].getName
      case "jmeter"              => classOf[JMeterCodegen].getName
      case "jaxrs-cxf-client"    => classOf[JavaCXFClientCodegen].getName
      case "jaxrs-cxf"           => classOf[JavaCXFServerCodegen].getName
      case "java"                => classOf[JavaClientCodegen].getName
      case "inflector"           => classOf[JavaInflectorServerCodegen].getName
      case "jaxrs-cxf-cdi"       =>
        classOf[JavaJAXRSCXFCDIServerCodegen].getName
      case "jaxrs-spec"          => classOf[JavaJAXRSSpecServerCodegen].getName
      case "jaxrs"               => classOf[JavaJAXRSSpecServerCodegen].getName
      case "msf4j"               => classOf[JavaMSF4JServerCodegen].getName
      case "java-pkmst"          => classOf[JavaPKMSTServerCodegen].getName
      case "java-play-framework" => classOf[JavaPlayFrameworkCodegen].getName
      case "jaxrs-resteasy-eap"  =>
        classOf[JavaResteasyEapServerCodegen].getName
      case "jaxrs-resteasy"      => classOf[JavaResteasyServerCodegen].getName
      case "javascript"          => classOf[JavascriptClientCodegen].getName
      case "javascript-closure-angular" =>
        classOf[JavascriptClosureAngularClientCodegen].getName
      case "java-vertx"         => classOf[JavaVertXServerCodegen].getName
      case "kotlin"             => classOf[KotlinClientCodegen].getName
      case "lua"                => classOf[LuaClientCodegen].getName
      case "lumen"              => classOf[LumenServerCodegen].getName
      case "nancyfx"            => classOf[NancyFXServerCodegen].getName
      case "nodejs-server"      => classOf[NodeJSServerCodegen].getName
      case "objc"               => classOf[ObjcClientCodegen].getName
      case "perl"               => classOf[PerlClientCodegen].getName
      case "php"                => classOf[PhpClientCodegen].getName
      case "powershell"         => classOf[PowerShellClientCodegen].getName
      case "pistache-server"    => classOf[PistacheServerCodegen].getName
      case "python"             => classOf[PythonClientCodegen].getName
      case "qt5cpp"             => classOf[Qt5CPPGenerator].getName
      case "r"                  => classOf[RClientCodegen].getName
      case "rails5"             => classOf[Rails5ServerCodegen].getName
      case "restbed"            => classOf[RestbedCodegen].getName
      case "ruby"               => classOf[RubyClientCodegen].getName
      case "rust"               => classOf[RustClientCodegen].getName
      case "rust-server"        => classOf[RustServerCodegen].getName
      case "scala"              => classOf[ScalaClientCodegen].getName
      case "scala-lagom-server" => classOf[ScalaLagomServerCodegen].getName
      case "scalatra"           => classOf[ScalatraServerCodegen].getName
      case "scalaz"             => classOf[ScalazClientCodegen].getName
      case "php-silex"          => classOf[SilexServerCodegen].getName
      case "sinatra"            => classOf[SinatraServerCodegen].getName
      case "slim"               => classOf[SlimFrameworkServerCodegen].getName
      case "spring"             => classOf[SpringCodegen].getName
      case "html2"              => classOf[StaticHtml2Generator].getName
      case "html"               => classOf[StaticHtmlGenerator].getName
      case "swagger"            => classOf[SwaggerCodegen].getName
      case "swagger-yaml"       => classOf[SwaggerYamlGenerator].getName
      case "swift4"             => classOf[Swift4Codegen].getName
      case "swift3"             => classOf[Swift3Codegen].getName
      case "swift"              => classOf[SwiftCodegen].getName
      case "php-symfony"        => classOf[SymfonyServerCodegen].getName
      case "tizen"              => classOf[TizenClientCodegen].getName
      case "typescript-aurelia" =>
        classOf[TypeScriptAureliaClientCodegen].getName
      case "typescript-angular" =>
        classOf[TypeScriptAngularClientCodegen].getName
      case "typescript-angularjs" =>
        classOf[TypeScriptAngularJsClientCodegen].getName
      case "typescript-fetch"  => classOf[TypeScriptFetchClientCodegen].getName
      case "typescript-jquery" => classOf[TypeScriptJqueryClientCodegen].getName
      case "typescript-node"   => classOf[TypeScriptNodeClientCodegen].getName
      case "undertow"          => classOf[UndertowCodegen].getName
      case "ze-ph"             =>
        classOf[ZendExpressivePathHandlerServerCodegen].getName
      case _ ⇒ lang
    }
  }
  // scalastyle:on

  def listOutputFiles(f: File, r: Regex): Array[File] = {
    Option(f.listFiles) match {
      case Some(these) ⇒
        val good = these.filter(f => r.findFirstIn(f.getName).isDefined)
        good ++ these.filter(_.isDirectory).flatMap(listOutputFiles(_, r))
      case None ⇒
        Array.empty[File]
    }
  }

def runSwaggerCodegen(
    langToUse: String,
    templatesDir: Option[File],
    sourceFile: File,
    outputDir: File,
    apiPackage: String,
    modelPackage: String,
    invokerPackage: String,
    generateWholeProject: Boolean,
    verbose: Boolean,
    skipOverwrite: Boolean,
    log: ManagedLogger
  ): Seq[File] = {

    val args: Seq[String] = {
      Seq(
        "generate",
        "-l",
        langToUse,
        "-i",
        sourceFile.getAbsolutePath,
        "-o",
        outputDir.getAbsolutePath,
        "--api-package",
        apiPackage,
        "--model-package",
        modelPackage,
        "--invoker-package",
        invokerPackage
      ) ++ {
        templatesDir match {
          case Some(dir) ⇒
            Seq("-t", dir.getAbsolutePath)
          case None ⇒
            Seq.empty[String]
        }
      } ++ {
        if (verbose) {
          Seq("-v")
        } else {
          Seq.empty[String]
        }
      } ++ {
        if (skipOverwrite) {
          Seq("--skip-overwrite")
        } else {
          Seq.empty[String]
        }
      }
    }

    Try {
      val argsArray = args.toArray[String]
      if (verbose) {
        System.setProperty("debugParser", "on")
      } else {
        System.clearProperty("debugParser")
      }
      SwaggerCodegen.main(argsArray)
    } match {
      case Success(_) ⇒
        if (generateWholeProject) {
          Seq.empty[File]
        } else {
          val od = outputDir.getAbsolutePath
          val regex = s"$od/(src|app|conf|public)$$".r
          outputDir
            .listFiles()
            .find { path ⇒
              regex.findFirstIn(path.getAbsolutePath).nonEmpty
            } match {
            case Some(top) ⇒
              val files = listOutputFiles(top, """.*[^.]\.[a-z]+""".r).toSeq
              files.filter(_.isFile)
            case None ⇒
              Seq.empty[File]
          }
        }
      case Failure(xcptn) ⇒
        log.error("SwaggerCodeGen failed: " +
          xcptn.getClass.getName + ": " + xcptn.getLocalizedMessage
        )
        throw xcptn
    }
  }
}
