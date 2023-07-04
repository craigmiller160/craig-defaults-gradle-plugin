package io.craigmiller160.gradle.plugins.publishing

import io.craigmiller160.gradle.plugins.testutils.GradleTestContext
import io.craigmiller160.gradle.plugins.testutils.GradleTestExtension
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import org.junit.jupiter.api.Assertions.assertTrue
import java.nio.file.Paths
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.io.path.exists
import kotlin.io.path.readText

@ExtendWith(GradleTestExtension::class)
class SetupPublishingTest {
  @Test
  fun `does not add publication when maven-publish task is not present`(
      context: GradleTestContext
  ) {
    val script =
        """
          plugins {
            id("io.craigmiller160.gradle.defaults") version "${context.pluginVersion}"
            kotlin("jvm") version "1.8.20"
          }
      """
            .trimIndent()
    context.writeBuildScript(script)

    val result = context.runner.withArguments("tasks").build()
    result.output.shouldNotContain("publishMavenPublicationToCraigNexusRepository")
  }

  @Test
  fun `adds publication when maven-publish task is present`(context: GradleTestContext) {
    val script =
        """
          plugins {
            id("io.craigmiller160.gradle.defaults") version "${context.pluginVersion}"
            kotlin("jvm") version "1.8.20"
            `maven-publish`
          }
      """
            .trimIndent()
    context.writeBuildScript(script)

    val result = context.runner.withArguments("tasks").build()
    result.output.shouldContain("publishMavenPublicationToCraigNexusRepository")
  }

  @Test
  fun `runs publish task and fixes pom_xml`(context: GradleTestContext) {
    val group = "io.craigmiller160.test"
    val version = "1.0.0"
    val script =
        """
          plugins {
            id("io.craigmiller160.gradle.defaults") version "${context.pluginVersion}"
            kotlin("jvm") version "1.8.20"
            `maven-publish`
          }
          
          group = "$group"
          version = "$version"
      """
            .trimIndent()
    context.writeBuildScript(script)

    val result = context.runner.withArguments("publishToMavenLocal").build()
    val pomPath =
        Paths.get(
            System.getProperty("user.home"),
            ".m2",
            "repository",
            group,
            GradleTestExtension.PROJECT_NAME,
            version,
            "${GradleTestExtension.PROJECT_NAME}.pom")
      assertTrue { pomPath.exists() }
      val xml = pomPath.readText()
      println("XML: $xml")

    // TODO don't forget about validating the pom.xml fix
  }
}
