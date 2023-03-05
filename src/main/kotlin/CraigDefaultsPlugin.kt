import org.gradle.api.Plugin
import org.gradle.api.Project
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

class CraigDefaultsPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register("installDefaultGitHooks") { task ->
            task.doLast { taskDoLast ->
                taskDoLast.logger.info("Installing Default Git Hooks")
                val hooksSourceDir = Paths.get(System.getProperty("user.home"), ".gradle", "gitHooks")
                val hooksTargetDir = Paths.get(project.rootDir.absolutePath, ".git", "hooks")
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
}