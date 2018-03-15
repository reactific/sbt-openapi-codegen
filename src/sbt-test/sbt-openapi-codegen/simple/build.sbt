enablePlugins(OpenApiCodeGenPlugin)

name := "simple-test"
version := "0.1"
scalaVersion := "2.12.4"
warningsAreErrors := true
codePackage := "com.reactific.api.test"
titleForDocs := "API Test"

openApiSpec := file("src/main/openapi/simple_test.yml")
