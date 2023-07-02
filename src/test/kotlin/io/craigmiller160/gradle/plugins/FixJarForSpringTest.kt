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

    @BeforeEach
    fun setup(@TempDir tempDir: File) {
        runner = GradleRunner.create()
            .withPluginClasspath()
            .withProjectDir(tempDir)

        if (!File(tempDir, "build.gradle.kts").createNewFile()) {
            throw RuntimeException("Cannot create temporary gradle build file")
        }
    }
    @Test
    fun experiment() {
        val result = runner.withArguments("tasks").build()
        println(result.output)
    }
}