addSbtPlugin("com.reactific" %% "sbt-reactific" % "3.0.5")

libraryDependencies ++= Seq(
  "org.scala-sbt" %% "scripted-plugin" % sbtVersion.value,
  "org.slf4j" % "slf4j-simple" % "1.7.25",
  "io.swagger" % "swagger-codegen-cli" % "2.3.1"
)
