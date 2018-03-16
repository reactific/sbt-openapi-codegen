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

object OpenApiCodeGenPlugin extends AutoPlugin {

  override def trigger = allRequirements
  
  object autoImport {

    val runSwaggerCodegenTask: TaskKey[Seq[File]] = TaskKey[Seq[File]](
      "generate-scala-lagom", "Task to generate scala-lagom code"
    )
  
    val codegenType: SettingKey[String] = settingKey[String](
      "The swagger-codegen-cli type of codegen per the -l option"
    )
    val openApiSpec: SettingKey[File] = settingKey[File](
      "The path to the OpenAPI Specification File to compile"
    )

    // -v --verbose
    val verbose: SettingKey[Boolean] = settingKey[Boolean](
      "Run the swagger-codegen-cli with verbose output option turned on"
    )

    // -s --skip-overwrite
    val skipOverwite: SettingKey[Boolean] = settingKey[Boolean](
      "specifies if the existing files should be " +
        "overwritten during the generation."
    )

    // -o --output
    val outputDirectory: SettingKey[Option[File]] = settingKey[Option[File]](
      "where to write the generated files (current dir by default)"
    )

    // --api-package
    val apiPackage: SettingKey[String] = settingKey[String](
      "name of package for the api output"
    )

    // --model-package
    val modelPackage: SettingKey[String] = settingKey[String](
      "name of package for the model output"
    )

    // --model-name-prefix
    val modelNamePrefix : SettingKey[String] = settingKey[String](
      "A prefix to prepend to each model name"
    )

    // --model-name-suffix
    val modelNameSuffix : SettingKey[String] = settingKey[String](
      "A suffix to append to each model name"
    )

  /* MORE OPTIONS TO CONSIDER FOR SwaggerCodegen CLI generate command

    @Option(name = Array(Array("-D")), title = "system properties", description = "sets specified system properties in " + "the format of name=value,name=value (or multiple options, each with name=value)")  private val systemProperties: List[String] = new ArrayList[String]

    @Option(name = Array(Array("-c", "--config")), title = "configuration file", description = "Path to json configuration file. " + "File content should be in a json format {\"optionKey\":\"optionValue\", \"optionKey1\":\"optionValue1\"...} " + "Supported options can be different for each language. Run config-help -l {lang} command for language specific config options.")  private val configFile: String = null

        @Option(name = Array(Array("-a", "--auth")), title = "authorization", description = "adds authorization headers when fetching the swagger definitions remotely. " + "Pass in a URL-encoded string of name:header with a comma separating multiple values")  private val auth: String = null

    @Option(name = Array(Array("-D")), title = "system properties", description = "sets specified system properties in " + "the format of name=value,name=value (or multiple options, each with name=value)")  private val systemProperties: List[String] = new ArrayList[String]

    @Option(name = Array(Array("-c", "--config")), title = "configuration file", description = "Path to json configuration file. " + "File content should be in a json format {\"optionKey\":\"optionValue\", \"optionKey1\":\"optionValue1\"...} " + "Supported options can be different for each language. Run config-help -l {lang} command for language specific config options.")  private val configFile: String = null

    @Option(name = Array(Array("-s", "--skip-overwrite")), title = "skip overwrite", description = "specifies if the existing files should be " + "overwritten during the generation.")  private val skipOverwrite: Boolean = false

    @Option(name = Array(Array("--api-package")), title = "api package", description = CodegenConstants.API_PACKAGE_DESC)  private val apiPackage: String = null

    @Option(name = Array(Array("--model-package")), title = "model package", description = CodegenConstants.MODEL_PACKAGE_DESC)  private val modelPackage: String = null

    @Option(name = Array(Array("--model-name-prefix")), title = "model name prefix", description = CodegenConstants.MODEL_NAME_PREFIX_DESC)  private val modelNamePrefix: String = null

    @Option(name = Array(Array("--model-name-suffix")), title = "model name suffix", description = CodegenConstants.MODEL_NAME_SUFFIX_DESC)  private val modelNameSuffix: String = null

    @Option(name = Array(Array("--instantiation-types")), title = "instantiation types", description = "sets instantiation type mappings in the format of type=instantiatedType,type=instantiatedType." + "For example (in Java): array=ArrayList,map=HashMap. In other words array types will get instantiated as ArrayList in generated code." + " You can also have multiple occurrences of this option.")  private val instantiationTypes: List[String] = new ArrayList[String]

    @Option(name = Array(Array("--type-mappings")), title = "type mappings", description = "sets mappings between swagger spec types and generated code types " + "in the format of swaggerType=generatedType,swaggerType=generatedType. For example: array=List,map=Map,string=String." + " You can also have multiple occurrences of this option.")  private val typeMappings: List[String] = new ArrayList[String]

    @Option(name = Array(Array("--additional-properties")), title = "additional properties", description = "sets additional properties that can be referenced by the mustache templates in the format of name=value,name=value." + " You can also have multiple occurrences of this option.")  private val additionalProperties: List[String] = new ArrayList[String]

    @Option(name = Array(Array("--language-specific-primitives")), title = "language specific primitives", description = "specifies additional language specific primitive types in the format of type1,type2,type3,type3. For example: String,boolean,Boolean,Double." + " You can also have multiple occurrences of this option.")  private val languageSpecificPrimitives: List[String] = new ArrayList[String]

    @Option(name = Array(Array("--import-mappings")), title = "import mappings", description = "specifies mappings between a given class and the import that should be used for that class in the format of type=import,type=import." + " You can also have multiple occurrences of this option.")  private val importMappings: List[String] = new ArrayList[String]

    @Option(name = Array(Array("--invoker-package")), title = "invoker package", description = CodegenConstants.INVOKER_PACKAGE_DESC)  private val invokerPackage: String = null

    @Option(name = Array(Array("--group-id")), title = "group id", description = CodegenConstants.GROUP_ID_DESC)  private val groupId: String = null

    @Option(name = Array(Array("--artifact-id")), title = "artifact id", description = CodegenConstants.ARTIFACT_ID_DESC)  private val artifactId: String = null

    @Option(name = Array(Array("--artifact-version")), title = "artifact version", description = CodegenConstants.ARTIFACT_VERSION_DESC)  private val artifactVersion: String = null

    @Option(name = Array(Array("--library")), title = "library", description = CodegenConstants.LIBRARY_DESC)  private val library: String = null

    @Option(name = Array(Array("--git-user-id")), title = "git user id", description = CodegenConstants.GIT_USER_ID_DESC)  private val gitUserId: String = null

    @Option(name = Array(Array("--git-repo-id")), title = "git repo id", description = CodegenConstants.GIT_REPO_ID_DESC)  private val gitRepoId: String = null

    @Option(name = Array(Array("--release-note")), title = "release note", description = CodegenConstants.RELEASE_NOTE_DESC)  private val releaseNote: String = null

    @Option(name = Array(Array("--http-user-agent")), title = "http user agent", description = CodegenConstants.HTTP_USER_AGENT_DESC)  private val httpUserAgent: String = null

    @Option(name = Array(Array("--reserved-words-mappings")), title = "reserved word mappings", description = "specifies how a reserved name should be escaped to. Otherwise, the default _<name> is used. For example id=identifier." + " You can also have multiple occurrences of this option.")  private val reservedWordsMappings: List[String] = new ArrayList[String]

    @Option(name = Array(Array("--ignore-file-override")), title = "ignore file override location", description = CodegenConstants.IGNORE_FILE_OVERRIDE_DESC)  private val ignoreFileOverride: String = null

    @Option(name = Array(Array("--remove-operation-id-prefix")), title = "remove prefix of the operationId", description = CodegenConstants.REMOVE_OPERATION_ID_PREFIX_DESC)  private val removeOperationIdPrefix: Boolean = false
    */
  }

  import autoImport._
  
  override lazy val globalSettings = Seq()
  
  override lazy val buildSettings = Seq(
    organization := "com.reactific",
    organizationHomepage := Some(url("https://www.reactific.com/")),
    organizationName := "Reactific Software LLC",
    scalaVersion := "2.12.4",
    startYear := Some(2018)
  )
  
  override lazy val projectSettings = Seq(
      sourceGenerators in Compile += (runSwaggerCodegenTask in Compile),
      runSwaggerCodegenTask in Compile := {
        runSwaggerCodegen(
          codegenType.value,
          openApiSpec.value,
          (sbt.Keys.managedSourceDirectories in Compile).value.head,
          outputDirectory.value,
          apiPackage.value, modelPackage.value,
          verbose.value, skipOverwite.value
        )
      },
      codegenType := "scala-lagom-server",
      openApiSpec := file(""),
      verbose := true,
      skipOverwite := true,
      outputDirectory := None,
      apiPackage := "your.package.name.api",
      modelPackage := "your.package.name.model",
      modelNamePrefix := "",
      modelNameSuffix := "",
      libraryDependencies += "io.swagger" % "swagger-codegen" % "2.3.1"
  )

  def listOutputFiles(f: File, r: Regex): Array[File] = {
    val these = f.listFiles
    val good = these.filter(f => r.findFirstIn(f.getName).isDefined)
    good ++ these.filter(_.isDirectory).flatMap(listOutputFiles(_,r))
  }

  def runSwaggerCodegen(
    language: String,
    sourceFile: File,
    generatedOutputBase: File,
    outputDirectory: Option[File],
    apiPackage: String,
    modelPackage: String,
    verbose: Boolean,
    skipOverwrite: Boolean
  ): Seq[File] = {
    val outputRoot: File = outputDirectory  match {
      case Some(dir) =>
        dir
      case None =>
        generatedOutputBase / "scala"
    }
    
    // FIXME: currently the SwaggerCodegen.main expects a path not a resource
    // FIXME: name in the jar file. We need to extract from the resource and
    // FIXME: create a tempdirectory and pass it for the -t option, probably
    // FIXME: the -l too.
    val args: Seq[String] = {
      Seq(
        "generate",
        // "-t", "scala-lagom-server",
        "-l", language,
        "-i", sourceFile.getAbsolutePath,
        "-o", outputRoot.getAbsolutePath,
        "--api-package", apiPackage,
        "--model-package", modelPackage
      ) ++ {
        if (verbose) { Seq("-v") } else { Seq[String]() }
      } ++ {
        if (skipOverwrite) { Seq("--skip-overwrite") } else { Seq[String]() }
      }
    }
  
    Try {
      SwaggerCodegen.main(args.toArray[String])
    } match {
      case Success(_) ⇒
        listOutputFiles(outputRoot,""".*\.scala""".r).toSeq
      case Failure(x) ⇒
        throw new RuntimeException(
          "SwaggerCodeGen failed: " +
            x.getClass.getName + ": " + x.getLocalizedMessage
        )
    }
  }


}
