package io.craigmiller160.gradle.plugins.building

import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.name

fun Project.fixJarForSpring() {
  afterEvaluate { project ->
    project.tasks.findByName("bootJar")?.let { rawBootJarTask ->

    project.tasks.getByName("jar") { rawJarTask ->
        val jarTask = rawJarTask as Jar
          jarTask.doLast {
              val libsDir = Paths.get(System.getProperty("user.dir"), "build", "libs")
              Files.list(libsDir)
                  .filter { file -> file.name.endsWith("-plain.jar") }
                  .forEach { Files.delete(it) }
          }
      }
    }
  }
}
