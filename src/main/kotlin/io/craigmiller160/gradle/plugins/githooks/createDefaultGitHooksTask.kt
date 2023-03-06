package io.craigmiller160.gradle.plugins.githooks

import org.gradle.api.Project
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

fun Project.createDefaultGitHooksTask() {
    tasks.register("installDefaultGitHooks") { task ->
        println("WORKING")
        println(resources.text.fromArchiveEntry("craig-defaults-gradle-plugin-1.0.0-SNAPSHOT", "githooks/pre-commit").asFile().readText())
//        resources.text.fromArchiveEntry("")
        task.doLast { taskDoLast ->
            taskDoLast.logger.info("Installing Default Git Hooks")
            val sourcePath = Paths.get(ClassLoader.getSystemClassLoader().getResource("/githooks/pre-commit").toURI())
            val targetPath = Paths.get(System.getProperty("user.dir"), ".git", "hooks", "pre-commit")
            Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES)
        }
    }
}