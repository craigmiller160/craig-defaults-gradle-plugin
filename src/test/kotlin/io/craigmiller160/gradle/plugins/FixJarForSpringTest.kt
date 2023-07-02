package io.craigmiller160.gradle.plugins

import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.lang.RuntimeException
import java.nio.file.Files

class FixJarForSpringTest {
    private lateinit var runner: GradleRunner
    private lateinit var buildFile: File

    @BeforeEach
    fun setup(@TempDir tempDir: File) {
        val testKitDir = File(tempDir, "testKit")
        if (!testKitDir.mkdirs()) {
            throw RuntimeException("Cannot create temporary gradle test kit directory")
        }

        runner = GradleRunner.create()
            .withPluginClasspath()
            .withProjectDir(tempDir)
            .withTestKitDir(testKitDir)

        buildFile = File(tempDir, "build.gradle.kts")

        if (!buildFile.createNewFile()) {
            throw RuntimeException("Cannot create temporary gradle build file")
        }
    }
    @Test
    fun experiment() {
        val result = runner.withArguments("tasks").build()
        println(result.output)
    }
}