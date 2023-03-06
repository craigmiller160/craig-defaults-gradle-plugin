package io.craigmiller160.gradle.plugins.publishing

import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication

fun Project.setupPublishing() {
    pluginManager.withPlugin("maven-publish") {
        extensions.configure<PublishingExtension>("publishing") { publishing ->
            publishing.publications { publication ->
                publication.create("maven") {
                    val pub = it as MavenPublication
                    pub.groupId = project.group.toString()
                    pub.artifactId = rootProject.name
                    pub.version = project.version.toString()

                    pub.from(components.getByName("kotlin"))
                }
            }
        }
    }
}