package io.craigmiller160.gradle.plugins.githooks

import io.craigmiller160.gradle.plugins.testutils.GradleTestContext
import io.craigmiller160.gradle.plugins.testutils.GradleTestExtension
import io.craigmiller160.gradle.plugins.testutils.shouldHaveExecuted
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.paths.shouldExist
import io.kotest.matchers.paths.shouldNotExist
import io.kotest.matchers.shouldBe
import java.nio.file.Paths
import java.nio.file.attribute.PosixFilePermission
import kotlin.io.path.createFile
import kotlin.io.path.getPosixFilePermissions
import kotlin.io.path.readText
import org.gradle.testkit.runner.TaskOutcome
import org.gradle.testkit.runner.internal.DefaultBuildTask
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(GradleTestExtension::class)
class SetupSpotlessGitHookTest {
  @Test
  fun `does nothing when spotless plugin is not present`(context: GradleTestContext) {
    val script =
        """
      plugins {
        id("io.craigmiller160.gradle.defaults") version "${context.pluginVersion}"
        kotlin("jvm") version "1.8.20"
      }
    """
            .trimIndent()
    context.writeBuildScript(script)

    val result = context.runner.withArguments("init").build()
    result.tasks.shouldHaveExecuted(DefaultBuildTask(":init", TaskOutcome.SKIPPED))

    val preCommitPath = context.workingDir.resolve(Paths.get(".git", "hooks", "pre-commit"))
    preCommitPath.shouldNotExist()
  }

  @Test
  fun `does nothing when spotless plugin is present but guard file is also present`(
      context: GradleTestContext
  ) {
    val script =
        """
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
      """
            .trimIndent()
    context.writeBuildScript(script)

    context.workingDir.resolve(Paths.get(".git", "hooks", GUARD_FILE)).apply { createFile() }

      val result = context.runner.withArguments("init").build()
      result.tasks.shouldHaveExecuted(DefaultBuildTask(":init", TaskOutcome.SKIPPED))

      val preCommitPath = context.workingDir.resolve(Paths.get(".git", "hooks", "pre-commit"))
      preCommitPath.shouldNotExist()
  }

  @Test
  fun `writes pre-commit git hook when spotless plugin is present`(context: GradleTestContext) {
    val script =
        """
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
      """
            .trimIndent()
    context.writeBuildScript(script)

    val result = context.runner.withArguments("init").build()
    result.tasks.shouldHaveExecuted(DefaultBuildTask(":init", TaskOutcome.SKIPPED))

    val preCommitPath = context.workingDir.resolve(Paths.get(".git", "hooks", "pre-commit"))
    preCommitPath.shouldExist()
    preCommitPath.readText().shouldBe(PRE_COMMIT)

    preCommitPath
        .getPosixFilePermissions()
        .shouldContain(
            setOf(
                PosixFilePermission.OWNER_READ,
                PosixFilePermission.OWNER_WRITE,
                PosixFilePermission.OWNER_EXECUTE,
                PosixFilePermission.GROUP_READ,
                PosixFilePermission.GROUP_EXECUTE,
                PosixFilePermission.OTHERS_READ,
                PosixFilePermission.OTHERS_EXECUTE))
  }
}
