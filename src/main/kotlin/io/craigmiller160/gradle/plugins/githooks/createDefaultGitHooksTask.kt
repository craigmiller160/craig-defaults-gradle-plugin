package io.craigmiller160.gradle.plugins.githooks

import org.gradle.api.Project
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

fun Project.createDefaultGitHooksTask() {
    tasks.register("installDefaultGitHooks") { task ->
        task.doLast { taskDoLast ->
            taskDoLast.logger.info("Installing Default Git Hooks")
            // TODO heavily refactor this logic
            val hooksSourceDir = Paths.get(System.getProperty("user.home"), ".gradle", "gitHooks")
            val hooksTargetDir = Paths.get(rootDir.absolutePath, ".git", "hooks")
            runCatching {
                Files.list(hooksSourceDir)
                    .forEach { hookPath ->
                        val hookGitPath = hooksTargetDir.resolve(hookPath.fileName)
                        Files.copy(hookPath, hookGitPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES)
                    }
            }
                .onFailure { it.printStackTrace() }
        }
    }
}