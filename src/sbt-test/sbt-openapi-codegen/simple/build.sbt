
enablePlugins(ReactificPlugin)
enablePlugins(OpenApiCodeGenPlugin)
name := "swagger-scala-client"
version := "1.0.0"
scalaVersion := "2.11.12"
codePackage := "com.reactific.sbt.simple"
titleForDocs := "Test Project"
verbose := true
warningsAreErrors := false
codegenType := "scala"
openApiSpec := file("src/main/openapi/simple_test.yml")
libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.7.25"

libraryDependencies ++= Seq(
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.9.2",
  "com.fasterxml.jackson.datatype" % "jackson-datatype-joda" % "2.9.2",
  "com.sun.jersey" % "jersey-core" % "1.19.4",
  "com.sun.jersey" % "jersey-client" % "1.19.4",
  "com.sun.jersey.contribs" % "jersey-multipart" % "1.19.4",
  "org.jfarcand" % "jersey-ahc-client" % "1.0.5",
  "io.swagger" % "swagger-core" % "1.5.8",
  "joda-time" % "joda-time" % "2.9.9",
  "org.joda" % "joda-convert" % "1.9.2",
  "org.scalatest" %% "scalatest" % "3.0.4" % "test",
  "junit" % "junit" % "4.12" % "test",
  "com.wordnik.swagger" %% "swagger-async-httpclient" % "0.3.5"
)
