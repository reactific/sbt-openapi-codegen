package com.reactific.sbt

import java.io.File

import org.scalatest.WordSpec
import sbt.internal.util.ManagedLogger
import org.apache.logging.log4j.core.LoggerContext

class OpenApiCodeGenPluginSpec extends WordSpec {

  "OpenApiCodeGenPlugin" when {
    "run" should {
      "return list of files produced" in {
        val l4j = new LoggerContext("test").getLogger("test")
        val log: ManagedLogger = new ManagedLogger("test", None, None, l4j)
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
          skipOverwrite = false,
          log
        )
        assert(files.nonEmpty)
        println("Source files")
        files.foreach{ f: File ⇒ println(f.getAbsolutePath)}
      }
    }
  }
}
