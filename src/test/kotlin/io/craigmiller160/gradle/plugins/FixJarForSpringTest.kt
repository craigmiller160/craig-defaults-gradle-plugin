package io.craigmiller160.gradle.plugins

import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.lang.RuntimeException

class FixJarForSpringTest {
    private lateinit var gradleRunner: GradleRunner
    private lateinit var buildFile: File

    @BeforeEach
    fun setup(@TempDir tempDir: File) {
        val testKitDir = File(tempDir, "testKit")
        if (!testKitDir.mkdirs()) {
            throw RuntimeException("Cannot create temporary gradle test kit directory")
        }

        gradleRunner = GradleRunner.create()
            .withPluginClasspath()
            .withProjectDir(tempDir)
            .withTestKitDir(testKitDir)

        buildFile = File(tempDir, "build.gradle.kts")

        if (!buildFile.createNewFile()) {
            throw RuntimeException("Cannot create temporary gradle build file")
        }
    }

    @Test
    fun `runs jar task when spring boot is not present`() {
        buildFile.appendText("""
            plugins {
                kotlin("jvm") version "1.8.20"
            }
        """.trimIndent())

        val result = gradleRunner.withArguments("jar").build()
        result.tasks.forEach { task -> println("TASK: ${task.path} ${task.outcome}") }
    }

    @Test
    fun `runs jar task when spring boot is present but bootJar is disabled`() {
        TODO()
    }

    @Test
    fun `disables jar task when spring boot is present`() {
        TODO()
    }
}