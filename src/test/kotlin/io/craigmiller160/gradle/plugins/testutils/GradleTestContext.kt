package io.craigmiller160.gradle.plugins.testutils

import org.gradle.testkit.runner.GradleRunner
import java.nio.file.Files
import java.nio.file.Path

class GradleTestContext(
    val runner: GradleRunner,
    val pluginVersion: String,
    val buildFile: Path,
) {
    fun writeBuildScript(script: String) {
        Files.write(buildFile, script.toByteArray())
    }
}
