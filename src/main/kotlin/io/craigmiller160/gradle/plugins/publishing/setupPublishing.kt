package io.craigmiller160.gradle.plugins.publishing

import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication

fun Project.setupPublishing() {
    afterEvaluate {
        pluginManager.withPlugin("maven-publish") {
            extensions.configure<PublishingExtension>("publishing") { publishing ->
                val groupId = this@setupPublishing.group.toString()
                val artifactId = this@setupPublishing.rootProject.name
                val version = this@setupPublishing.version.toString()

                publishing.publications { publication ->
                    publication.create("maven", MavenPublication::class.java) { pub ->
                        pub.groupId = groupId
                        pub.artifactId = artifactId
                        pub.version = version

                        pub.from(components.getByName("kotlin"))
                    }
                }

                publishing.repositories { repoHandler ->
                    repoHandler.maven { mavenRepo ->
                        val repo = if (version.endsWith("-SNAPSHOT")) "maven-snapshots" else "maven-releases"
                        mavenRepo.url = uri("https://nexus-craigmiller160.ddns.net/repository/$repo")
                        mavenRepo.credentials { creds ->
                            creds.username = System.getenv("NEXUS_USER")
                            creds.password = System.getenv("NEXUS_PASSWORD")
                        }
                    }
                }
            }
        }
    }
}