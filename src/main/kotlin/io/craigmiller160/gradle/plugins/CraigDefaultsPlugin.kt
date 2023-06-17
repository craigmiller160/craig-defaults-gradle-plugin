package io.craigmiller160.gradle.plugins

import io.craigmiller160.gradle.plugins.building.fixJarForSpring
import io.craigmiller160.gradle.plugins.githooks.createSpotlessGitHook
import io.craigmiller160.gradle.plugins.publishing.fixMavenDependencyManagement
import io.craigmiller160.gradle.plugins.publishing.setupPublishing
import org.gradle.api.Plugin
import org.gradle.api.Project

class CraigDefaultsPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    project.createSpotlessGitHook()
    project.setupPublishing()
    project.fixJarForSpring()
  }
}
