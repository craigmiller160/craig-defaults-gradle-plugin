package io.craigmiller160.gradle.plugins

import io.craigmiller160.gradle.plugins.testutils.shouldHaveExecuted
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.gradle.testkit.runner.internal.DefaultBuildTask
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
                id("io.craigmiller160.gradle.defaults") version "1.3.0-SNAPSHOT"
                kotlin("jvm") version "1.8.20"
            }
        """.trimIndent())

        val result = gradleRunner.withArguments("jar").build()
        result.tasks.shouldHaveExecuted(
            DefaultBuildTask(":compileKotlin", TaskOutcome.NO_SOURCE),
            DefaultBuildTask(":compileJava", TaskOutcome.NO_SOURCE),
            DefaultBuildTask(":processResources", TaskOutcome.NO_SOURCE),
            DefaultBuildTask(":classes", TaskOutcome.UP_TO_DATE),
            DefaultBuildTask(":jar", TaskOutcome.SUCCESS)
        )
    }

    @Test
    fun `runs jar task when spring boot is present but bootJar is disabled`() {
        buildFile.appendText("""
            import org.springframework.boot.gradle.tasks.bundling.BootJar
            
            plugins {
                id("io.craigmiller160.gradle.defaults") version "1.3.0-SNAPSHOT"
                kotlin("jvm") version "1.8.20"
                id("org.springframework.boot") version "3.0.4"
                id("io.spring.dependency-management") version "1.1.0"
            }
            
            repositories {
                mavenCentral()
            }
            
            tasks.withType<BootJar> {
                enabled = false                   
            }
        """.trimIndent())

        val result = gradleRunner.withArguments("jar").build()
        result.tasks.shouldHaveExecuted(
            DefaultBuildTask(":compileKotlin", TaskOutcome.NO_SOURCE),
            DefaultBuildTask(":compileJava", TaskOutcome.NO_SOURCE),
            DefaultBuildTask(":processResources", TaskOutcome.NO_SOURCE),
            DefaultBuildTask(":classes", TaskOutcome.UP_TO_DATE),
            DefaultBuildTask(":jar", TaskOutcome.SUCCESS)
        )
    }

    @Test
    fun `disables jar task when spring boot is present`() {
        buildFile.appendText("""
            import org.springframework.boot.gradle.tasks.bundling.BootJar
            
            plugins {
                id("io.craigmiller160.gradle.defaults") version "1.3.0-SNAPSHOT"
                kotlin("jvm") version "1.8.20"
                id("org.springframework.boot") version "3.0.4"
                id("io.spring.dependency-management") version "1.1.0"
            }
            
            repositories {
                mavenCentral()
            }
        """.trimIndent())

        val result = gradleRunner.withArguments("jar").build()
        result.tasks.shouldHaveExecuted(
            DefaultBuildTask(":compileKotlin", TaskOutcome.NO_SOURCE),
            DefaultBuildTask(":compileJava", TaskOutcome.NO_SOURCE),
            DefaultBuildTask(":processResources", TaskOutcome.NO_SOURCE),
            DefaultBuildTask(":classes", TaskOutcome.UP_TO_DATE),
            DefaultBuildTask(":jar", TaskOutcome.SKIPPED)
        )
    }
}