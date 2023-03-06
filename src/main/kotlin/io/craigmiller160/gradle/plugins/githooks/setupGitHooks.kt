package io.craigmiller160.gradle.plugins.githooks

import org.gradle.api.Project
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.attribute.PosixFilePermission

private const val PRE_COMMIT = """
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

private const val GUARD_FILE = "craig-hooks-v1"

private fun writePreCommitFile() {
    val targetPath = Paths.get(System.getProperty("user.dir"), ".git", "hooks", "pre-commit")
    Files.write(targetPath, PRE_COMMIT.toByteArray())
    Files.setPosixFilePermissions(targetPath, setOf(
        PosixFilePermission.OWNER_READ,
        PosixFilePermission.OWNER_WRITE,
        PosixFilePermission.OWNER_EXECUTE,
        PosixFilePermission.GROUP_READ,
        PosixFilePermission.GROUP_EXECUTE,
        PosixFilePermission.OTHERS_READ,
        PosixFilePermission.OTHERS_EXECUTE
    ))
}

fun Project.createDefaultGitHooksTask() {
    val guardFile = Paths.get(".git", "hooks", GUARD_FILE)
    if (Files.exists(guardFile)) {
        logger.debug("Default git hooks already exist, skipping creating")
    } else {
        logger.info("Installing default Git hooks")
        writePreCommitFile()
        Files.createFile(guardFile)
    }
}