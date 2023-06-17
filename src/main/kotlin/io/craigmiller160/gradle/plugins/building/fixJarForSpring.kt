package io.craigmiller160.gradle.plugins.building

import org.gradle.api.Project

fun Project.fixJarForSpring() {
  afterEvaluate { project ->
    project.tasks.findByName("bootJar")?.let {
      project.tasks.getByName("jar") { rawJarTask ->
        rawJarTask.enabled = false
      }
    }
  }
}
