package io.craigmiller160.gradle.plugins.testutils

import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import java.nio.file.Files

class GradleTestExtension : BeforeEachCallback, AfterEachCallback {
    private val tempDir = Files.createTempDirectory("temp")
    override fun beforeEach(context: ExtensionContext) {
        val testKitDir = tempDir.resolve("testKit")
        Files.createDirectories(testKitDir)

        val gradleRunner =
            GradleRunner.create()
                .withPluginClasspath()
                .withProjectDir(tempDir.toFile())
                .withTestKitDir(testKitDir.toFile())

        val buildFile = tempDir.resolve("build.gradle.kts")
        Files.createFile(buildFile)
    }

    override fun afterEach(context: ExtensionContext) {

        TODO("Not yet implemented")
    }
}