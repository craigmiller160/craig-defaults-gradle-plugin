package io.craigmiller160.gradle.plugins.testutils

import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ExtensionContext.Namespace
import java.nio.file.Files
import java.nio.file.Path

class GradleTestExtension : BeforeEachCallback, AfterEachCallback {
    companion object {
        private const val WORKING_DIR_KEY = "WORKING_DIR"
    }

    private val tempDirRoot = Files.createTempDirectory("tempRoot")

    override fun beforeEach(context: ExtensionContext) {
        val workingDir = tempDirRoot.resolve("workingDir")
            .let { Files.createDirectories(it) }
        val testKitDir = workingDir.resolve("testKit")
            .let { Files.createDirectories(it) }

//        val gradleRunner =
//            GradleRunner.create()
//                .withPluginClasspath()
//                .withProjectDir(workingDir.toFile())
//                .withTestKitDir(testKitDir.toFile())

        val buildFile = workingDir.resolve("build.gradle.kts")
            .let { Files.createFile(it) }

        context.getStore(Namespace.create(GradleTestExtension::class.java))
            .let { store ->
                store.put(WORKING_DIR_KEY, workingDir)
            }
    }

    override fun afterEach(context: ExtensionContext) {
        val workingDir = context.getStore(Namespace.create(GradleTestExtension::class.java))
            .let { store ->
                store.get(WORKING_DIR_KEY) as Path
            }
        Files.delete(workingDir)
    }
}