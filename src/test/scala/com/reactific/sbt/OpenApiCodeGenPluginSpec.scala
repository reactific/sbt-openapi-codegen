package com.reactific.sbt

import java.io.File

import org.scalatest.WordSpec

class OpenApiCodeGenPluginSpec extends WordSpec {

  "OpenApiCodeGenPlugin" when {
    "run" should {
      "return list of files produced" in {
        val files = OpenApiCodeGenPlugin.runSwaggerCodegen(
          "akka-scala",
          None,
          new File("src/test/openapi/test.yaml"),
          new File("target/test/openapi"),
          "test.api",
          "test.model",
          "test.invoker",
          generateWholeProject = false,
          verbose = true,
          skipOverwrite = false
        )
        assert(files.nonEmpty)
        println("Source files")
        files.foreach{ f: File ⇒ println(f.getAbsolutePath)}
      }
    }
  }
}
