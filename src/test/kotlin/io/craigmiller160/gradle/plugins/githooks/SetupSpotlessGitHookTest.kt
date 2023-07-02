package io.craigmiller160.gradle.plugins.githooks

import io.craigmiller160.gradle.plugins.testutils.GradleTestContext
import io.craigmiller160.gradle.plugins.testutils.GradleTestExtension
import io.craigmiller160.gradle.plugins.testutils.shouldHaveExecuted
import io.kotest.matchers.paths.shouldExist
import io.kotest.matchers.paths.shouldNotExist
import org.gradle.testkit.runner.TaskOutcome
import org.gradle.testkit.runner.internal.DefaultBuildTask
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.nio.file.Paths

@ExtendWith(GradleTestExtension::class)
class SetupSpotlessGitHookTest {
  @Test
  fun `does nothing when spotless plugin is not present`(context: GradleTestContext) {
    val script = """
      plugins {
        id("io.craigmiller160.gradle.defaults") version "${context.pluginVersion}"
        kotlin("jvm") version "1.8.20"
      }
    """.trimIndent()
    context.writeBuildScript(script)

    val result = context.runner.withArguments("init").build()
    result.tasks.shouldHaveExecuted(
      DefaultBuildTask(":init", TaskOutcome.SKIPPED)
    )

    val preCommitPath = context.workingDir.resolve(Paths.get(".git", "hooks", "pre-commit"))
    preCommitPath.shouldNotExist()
  }

  @Test
  fun `does nothing when spotless plugin is present but guard file is also present`(context: GradleTestContext) {
      val script = """
          import com.diffplug.gradle.spotless.SpotlessExtension
          
          plugins {
            id("io.craigmiller160.gradle.defaults") version "${context.pluginVersion}"
            kotlin("jvm") version "1.8.20"
            id("com.diffplug.spotless") version "6.17.0"
          }
          
          configure<SpotlessExtension> {
            kotlin {
              ktfmt()
            }
          }
      """.trimIndent()
      context.writeBuildScript(script)


    TODO()
  }

  @Test
  fun `writes pre-commit git hook when spotless plugin is present`(context: GradleTestContext) {
      val script = """
          import com.diffplug.gradle.spotless.SpotlessExtension
          
          plugins {
            id("io.craigmiller160.gradle.defaults") version "${context.pluginVersion}"
            kotlin("jvm") version "1.8.20"
            id("com.diffplug.spotless") version "6.17.0"
          }
          
          configure<SpotlessExtension> {
            kotlin {
              ktfmt()
            }
          }
      """.trimIndent()
      context.writeBuildScript(script)

      val result = context.runner.withArguments("compileKotlin", "-i").build()
//      result.tasks.shouldHaveExecuted(
//          DefaultBuildTask(":init", TaskOutcome.SKIPPED)
//      )

      println(result.output)

      val preCommitPath = context.workingDir.resolve(Paths.get(".git", "hooks", "pre-commit"))
      preCommitPath.shouldExist()
  }
}
