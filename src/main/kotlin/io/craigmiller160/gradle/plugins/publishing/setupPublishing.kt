package io.craigmiller160.gradle.plugins.publishing

import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication

fun Project.setupPublishing() {
  afterEvaluate { project ->
    pluginManager.withPlugin("maven-publish") {
      project.setupPublication()
      project.fixMavenDependencyManagement()
    }
  }
}
