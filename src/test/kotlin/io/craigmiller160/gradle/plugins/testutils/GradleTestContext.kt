package io.craigmiller160.gradle.plugins.testutils

import java.nio.file.Files
import java.nio.file.Path
import org.gradle.testkit.runner.GradleRunner

class GradleTestContext(
    val runner: GradleRunner,
    val pluginVersion: String,
    val buildFile: Path,
    val workingDir: Path
) {
  fun writeBuildScript(script: String) {
    Files.write(buildFile, script.toByteArray())
  }
}
