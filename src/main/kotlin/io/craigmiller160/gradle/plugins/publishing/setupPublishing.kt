package io.craigmiller160.gradle.plugins.publishing

import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication

fun Project.setupPublishing() {
    pluginManager.withPlugin("maven-publish") {
        extensions.configure<PublishingExtension>("publishing") { publishing ->
            publishing.publications { publication ->
                publication.create("maven", MavenPublication::class.java) { pub ->
                    pub.groupId = this@setupPublishing.group.toString()
                    pub.artifactId = this@setupPublishing.rootProject.name
                    pub.version = this@setupPublishing.version.toString()

                    pub.from(components.getByName("kotlin"))
                }
            }
        }
    }
}