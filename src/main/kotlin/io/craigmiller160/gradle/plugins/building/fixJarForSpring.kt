package io.craigmiller160.gradle.plugins.building

import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar

fun Project.fixJarForSpring() {
  afterEvaluate { project ->
    project.tasks.findByName("bootJar")?.let { rawBootJarTask ->
      val bootJarTask = rawBootJarTask as Jar;

      project.tasks.getByName("jar") { rawJarTask ->
        val jarTask = rawJarTask as Jar
        jarTask.archiveClassifier.set("")
      }
    }
  }
}
