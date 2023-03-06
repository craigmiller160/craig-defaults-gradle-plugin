package io.craigmiller160.gradle.plugins

import io.craigmiller160.gradle.plugins.githooks.createDefaultGitHooksTask
import io.craigmiller160.gradle.plugins.publishing.setupPublishing
import org.gradle.api.Plugin
import org.gradle.api.Project

class CraigDefaultsPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.createDefaultGitHooksTask()
        project.setupPublishing()
    }
}