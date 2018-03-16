package com.reactific.sbt

import java.io.File

import org.scalatest.WordSpec

class OpenApiCodeGenPluginSpec extends WordSpec {

  "OpenApiCodeGenPlugin" when {
    "run" should {
      "do something" in {
        val files = OpenApiCodeGenPlugin.runSwaggerCodegen(
          "scala-lagom-server",
          new File("src/test/openapi/test.yaml"),
          new File("target/test/openapicodegen"),
          None,
          "test.api",
          "test.model",
          false,
          true
        )
        assert(files.nonEmpty)
        files.foreach{ f: File ⇒ println(f.getAbsolutePath)}
      }
    }
  }

}
