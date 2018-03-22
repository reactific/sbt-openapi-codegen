# sbt-openapi-codegen
An SBT Plugin to run swagger-codegen in sbt projects

## Purpose
This plugin makes working with swagger-codegen easier in sbt projects by 
handling the invocation of swagger-codegen-cli programmatically. An entire
project can be generated or just the source code and resources. 

## Installation
Add this to your `project/plugins.sbt`
```sbtshell
addSbtPlugin("com.reactific" %% "sbt-openapi-codegen" % "0.1.1"
```

## Usage
This plugin includes swagger-codegen-cli and allows you to invoke it from 
within a plugin to generate code. As such, it has many arguments that 
correspond to the swager-codegen-cli arguments, and a few additional ones:

* `generateWholeProject: Boolean` - Controls whether an entire separate 
project (true) is generated for separate compilation or the generated sources
and resources are added to the current build (false). The default is false.


* `codegenType: String` - corresponds to `-l` and specifies the kind of code to 
generate. You can use the Java classname of the generator or any of the 
following special values:
    * scala-lagom-server
    * scalatra-server
    * akka-scala-client
    * scala-client
    * scalaz-client
    * java-play-server

* `codegenConfigFile: File` - corresponds to `-c` option and provides the path
to more configuration options for the kind of `codegenType` selected
  
* `openApiSpec: File` - corresponds to `-i` option and provides the path to 
the OpenAPI Specification input file to compile 
* `templatesDir: Option[File]` - corresponds to `-t` option and optionally
provides the path to the directory containing the moustache templates to 
use for code generation. If 'None', it uses the default templates

* `outputDir: Option[File]` - corresponds to `-o` option and specifies the path
in which to put out the generated files. Defaults to `managedSourceRoot` under
the `openapi` directory. 

* `apiPackage: String` - corresponds to `--api-package` option and specifies 
the name of the package into which api code is generated.

* `modelPackage: String` - corresponds to `--model-package` option and specifies
the name of the package into which model code is generated. 

* `invokerPackage: String` - coresponds to `--invoker-package` option and
specifies the name of the package into which invoker code is generated. 

* `modelNamePrefix: String` - corresponds to `--model-name-prefix` option and
specifies a prefix to prepend to each model name

* `modelNameSuffix: String` - corresponds to `--model-name-suffix` optoin and
specifies a suffix to append to each model name

* `verbose: Boolean` - corresponds to `-v` option and runs swagger-codegen-cli
with verbose output option turned on

* `skipOverwrite: Boolean` - corresponds to `--skip-overwrite` and specifies
if the existing files should be overwritten during the generation.

