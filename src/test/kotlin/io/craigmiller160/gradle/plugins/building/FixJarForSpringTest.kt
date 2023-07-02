package io.craigmiller160.gradle.plugins.building

import io.craigmiller160.gradle.plugins.testutils.GradleTestExtension
import io.craigmiller160.gradle.plugins.testutils.shouldHaveExecuted
import java.nio.file.Files
import java.nio.file.Path
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.gradle.testkit.runner.internal.DefaultBuildTask
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(GradleTestExtension::class)
class FixJarForSpringTest(
    private val gradleRunner: GradleRunner,
    private val gradleBuildFile: Path
) {

  @Test
  fun `runs jar task when spring boot is not present`() {
    val script =
        """
          plugins {
            id("io.craigmiller160.gradle.defaults") version "1.3.0-SNAPSHOT"
            kotlin("jvm") version "1.8.20"
          }
      """
            .trimIndent()

    Files.write(gradleBuildFile, script.toByteArray())

    val result = gradleRunner.withArguments("jar").build()
    result.tasks.shouldHaveExecuted(
        DefaultBuildTask(":compileKotlin", TaskOutcome.NO_SOURCE),
        DefaultBuildTask(":compileJava", TaskOutcome.NO_SOURCE),
        DefaultBuildTask(":processResources", TaskOutcome.NO_SOURCE),
        DefaultBuildTask(":classes", TaskOutcome.UP_TO_DATE),
        DefaultBuildTask(":jar", TaskOutcome.SUCCESS))
  }

  @Test
  fun `runs jar task when spring boot is present but bootJar is disabled`() {
    val script =
        """
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
      """
            .trimIndent()
    Files.write(gradleBuildFile, script.toByteArray())

    val result = gradleRunner.withArguments("jar").build()
    result.tasks.shouldHaveExecuted(
        DefaultBuildTask(":compileKotlin", TaskOutcome.NO_SOURCE),
        DefaultBuildTask(":compileJava", TaskOutcome.NO_SOURCE),
        DefaultBuildTask(":processResources", TaskOutcome.NO_SOURCE),
        DefaultBuildTask(":classes", TaskOutcome.UP_TO_DATE),
        DefaultBuildTask(":jar", TaskOutcome.SUCCESS))
  }

  @Test
  fun `disables jar task when spring boot is present`() {
    val script =
        """
          plugins {
            id("io.craigmiller160.gradle.defaults") version "1.3.0-SNAPSHOT"
            kotlin("jvm") version "1.8.20"
            id("org.springframework.boot") version "3.0.4"
            id("io.spring.dependency-management") version "1.1.0"
          }
          
          repositories {
            mavenCentral()
          }
      """
            .trimIndent()
    Files.write(gradleBuildFile, script.toByteArray())

    val result = gradleRunner.withArguments("jar").build()
    result.tasks.shouldHaveExecuted(
        DefaultBuildTask(":compileKotlin", TaskOutcome.NO_SOURCE),
        DefaultBuildTask(":compileJava", TaskOutcome.NO_SOURCE),
        DefaultBuildTask(":processResources", TaskOutcome.NO_SOURCE),
        DefaultBuildTask(":classes", TaskOutcome.UP_TO_DATE),
        DefaultBuildTask(":jar", TaskOutcome.SKIPPED))
  }
}
