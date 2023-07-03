package io.craigmiller160.gradle.plugins.publishing

import io.craigmiller160.gradle.plugins.testutils.GradleTestContext
import io.craigmiller160.gradle.plugins.testutils.GradleTestExtension
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

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
    // TODO can I inspect the build file itself
  }

  @Test
  fun `fixes maven dependency management when generating pom_xml`(context: GradleTestContext) {
    TODO()
  }
}
