package io.craigmiller160.gradle.plugins.githooks

import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.attribute.PosixFilePermission
import org.gradle.api.Project

const val PRE_COMMIT =
    """
#!/bin/sh

echo "Running spotless check..."

files=${'$'}(git diff --name-only --staged)

./gradlew spotlessApply
status=${'$'}?

if [ ${'$'}status -ne 0 ]; then
  exit 1
fi

for file in ${'$'}files; do
  git add ${'$'}file
done

exit 0
"""

const val GUARD_FILE = "craig-hooks-v1"

private fun writePreCommitFile(projectDir: File) {
  val targetPath = projectDir.toPath().resolve(Paths.get(".git", "hooks", "pre-commit"))
  Files.write(targetPath, PRE_COMMIT.toByteArray())
  Files.setPosixFilePermissions(
      targetPath,
      setOf(
          PosixFilePermission.OWNER_READ,
          PosixFilePermission.OWNER_WRITE,
          PosixFilePermission.OWNER_EXECUTE,
          PosixFilePermission.GROUP_READ,
          PosixFilePermission.GROUP_EXECUTE,
          PosixFilePermission.OTHERS_READ,
          PosixFilePermission.OTHERS_EXECUTE))
}

fun Project.createSpotlessGitHook() {
  pluginManager.withPlugin("com.diffplug.spotless") {
    val guardFile = projectDir.toPath().resolve(Paths.get(".git", "hooks", GUARD_FILE))
    if (Files.exists(guardFile)) {
      logger.debug("Default git hooks already exist, skipping creating")
    } else {
      logger.info("Installing default Git hooks")
      writePreCommitFile(projectDir)
      Files.createFile(guardFile)
    }
  }
}
