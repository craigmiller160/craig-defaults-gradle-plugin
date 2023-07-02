package io.craigmiller160.gradle.plugins.testutils

import org.apache.commons.io.FileUtils
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ExtensionContext.Namespace
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver
import java.lang.IllegalArgumentException
import java.nio.file.Files
import java.nio.file.Path

class GradleTestExtension : BeforeEachCallback, AfterEachCallback, ParameterResolver {
    companion object {
        private const val WORKING_DIR_KEY = "WORKING_DIR"
        private const val BUILD_FILE_KEY = "BUILD_FILE"
        private const val GRADLE_RUNNER_KEY = "GRADLE_RUNNER"
    }

    private val tempDirRoot = Files.createTempDirectory("tempRoot")

    override fun beforeEach(context: ExtensionContext) {
        val workingDir = tempDirRoot.resolve("workingDir")
            .let { Files.createDirectories(it) }
        val testKitDir = workingDir.resolve("testKit")
            .let { Files.createDirectories(it) }

        val gradleRunner =
            GradleRunner.create()
                .withPluginClasspath()
                .withProjectDir(workingDir.toFile())
                .withTestKitDir(testKitDir.toFile())

        val buildFile = workingDir.resolve("build.gradle.kts")
            .let { Files.createFile(it) }

        context.getStore(Namespace.create(GradleTestExtension::class.java))
            .let { store ->
                store.put(WORKING_DIR_KEY, workingDir)
                store.put(BUILD_FILE_KEY, buildFile)
                store.put(GRADLE_RUNNER_KEY, gradleRunner)
            }
    }

    override fun afterEach(context: ExtensionContext) {
        val workingDir = context.getStore(Namespace.create(GradleTestExtension::class.java))
            .let { store ->
                store.get(WORKING_DIR_KEY) as Path
            }
        FileUtils.deleteDirectory(workingDir.toFile())

        context.getStore(Namespace.create(GradleTestExtension::class.java))
            .let { store ->
                store.remove(WORKING_DIR_KEY)
                store.remove(BUILD_FILE_KEY)
                store.remove(GRADLE_RUNNER_KEY)
            }
    }

    override fun supportsParameter(parameterContext: ParameterContext,
                                   extensionContext: ExtensionContext): Boolean =
        parameterContext.parameter.type == GradleRunner::class.java ||
                parameterContext.parameter.type == Path::class.java

    override fun resolveParameter(parameterContext: ParameterContext,
                                  extensionContext: ExtensionContext): Any {
        val store = extensionContext.getStore(Namespace.create(GradleTestExtension::class.java))
        return when (parameterContext.parameter.type) {
            GradleRunner::class.java -> store.get(GRADLE_RUNNER_KEY) as GradleRunner
            Path::class.java -> store.get(BUILD_FILE_KEY) as Path
            else -> throw IllegalArgumentException("Invalid parameter type: ${parameterContext.parameter.type}")
        }
    }
}