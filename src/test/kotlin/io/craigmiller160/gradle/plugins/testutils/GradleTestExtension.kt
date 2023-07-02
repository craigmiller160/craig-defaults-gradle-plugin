package io.craigmiller160.gradle.plugins.testutils

import java.lang.IllegalArgumentException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.Properties
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile
import kotlin.io.path.inputStream
import org.apache.commons.io.FileUtils
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ExtensionContext.Namespace
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver

class GradleTestExtension :
    BeforeEachCallback, AfterEachCallback, ParameterResolver, BeforeAllCallback, AfterAllCallback {
  companion object {
    private const val WORKING_DIR_KEY = "WORKING_DIR"
    private const val BUILD_FILE_KEY = "BUILD_FILE"
    private const val GRADLE_RUNNER_KEY = "GRADLE_RUNNER"
    private const val PLUGIN_VERSION_KEY = "PLUGIN_VERSION"
  }

  private val tempDirRoot = Files.createTempDirectory("tempRoot")

  override fun beforeEach(context: ExtensionContext) {
    val workingDir = tempDirRoot.resolve("workingDir").apply { createDirectories() }
    workingDir.resolve(Paths.get(".git", "hooks")).apply { createDirectories() }
    val testKitDir = workingDir.resolve("testKit").apply { createDirectories() }

    val gradleRunner =
        GradleRunner.create()
            .withPluginClasspath()
            .withProjectDir(workingDir.toFile())
            .withTestKitDir(testKitDir.toFile())

    val buildFile = workingDir.resolve("build.gradle.kts").apply { createFile() }

    context.getStore(Namespace.create(GradleTestExtension::class.java)).let { store ->
      store.put(WORKING_DIR_KEY, workingDir)
      store.put(BUILD_FILE_KEY, buildFile)
      store.put(GRADLE_RUNNER_KEY, gradleRunner)
    }
  }

  override fun afterEach(context: ExtensionContext) {
    val workingDir =
        context.getStore(Namespace.create(GradleTestExtension::class.java)).let { store ->
          store.get(WORKING_DIR_KEY) as Path
        }
    FileUtils.deleteDirectory(workingDir.toFile())

    context.getStore(Namespace.create(GradleTestExtension::class.java)).let { store ->
      store.remove(WORKING_DIR_KEY)
      store.remove(BUILD_FILE_KEY)
      store.remove(GRADLE_RUNNER_KEY)
    }
  }

  override fun beforeAll(context: ExtensionContext) {
    val gradleProperties = Paths.get(System.getProperty("user.dir"), "gradle.properties")
    val properties = Properties().apply { load(gradleProperties.inputStream()) }

    context.getStore(Namespace.create(GradleTestExtension::class.java)).let { store ->
      store.put(PLUGIN_VERSION_KEY, properties.getProperty("projectVersion"))
    }
  }

  override fun afterAll(context: ExtensionContext) {
    context.getStore(Namespace.create(GradleTestExtension::class.java)).let { store ->
      store.remove(PLUGIN_VERSION_KEY)
    }
  }

  override fun supportsParameter(
      parameterContext: ParameterContext,
      extensionContext: ExtensionContext
  ): Boolean = parameterContext.parameter.type == GradleTestContext::class.java

  override fun resolveParameter(
      parameterContext: ParameterContext,
      extensionContext: ExtensionContext
  ): Any {
    val store = extensionContext.getStore(Namespace.create(GradleTestExtension::class.java))
    return when (parameterContext.parameter.type) {
      GradleTestContext::class.java -> {
        val runner = store.get(GRADLE_RUNNER_KEY) as GradleRunner
        val buildFile = store.get(BUILD_FILE_KEY) as Path
        GradleTestContext(
            runner = store.get(GRADLE_RUNNER_KEY) as GradleRunner,
            buildFile = store.get(BUILD_FILE_KEY) as Path,
            pluginVersion = store.get(PLUGIN_VERSION_KEY) as String,
            workingDir = store.get(WORKING_DIR_KEY) as Path)
      }
      else ->
          throw IllegalArgumentException(
              "Invalid parameter type: ${parameterContext.parameter.type}")
    }
  }
}
