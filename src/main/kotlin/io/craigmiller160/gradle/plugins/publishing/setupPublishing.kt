package io.craigmiller160.gradle.plugins.publishing

import org.gradle.api.Project

fun Project.setupPublishing() {
  afterEvaluate { project ->
    pluginManager.withPlugin("maven-publish") {
      project.setupPublication()
      project.fixMavenDependencyManagement()
      project.fixPublishOrder()
    }
  }
}
