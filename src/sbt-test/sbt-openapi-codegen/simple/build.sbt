enablePlugins(OpenApiCodeGenPlugin)
enablePlugins(ReactificPlugin)

name := "simple-test"
version := "0.1"
scalaVersion := "2.12.4"
codePackage := "com.reactific.sbt.simple"
titleForDocs := "Test Project"
codegenType := "scala-lagom-server"
openApiSpec := file("src/main/openapi/simple_test.yml")
