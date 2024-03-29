package io.craigmiller160.gradle.plugins.publishing

import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication

private fun Project.isSpringBootApp(): Boolean {
  val bootJarTask = project.tasks.findByName("bootJar")
  return bootJarTask != null && bootJarTask.enabled
}

fun Project.setupPublication() {
  extensions.configure<PublishingExtension>("publishing") { publishing ->
    val groupId = this@setupPublication.group.toString()
    val artifactId = this@setupPublication.rootProject.name
    val version = this@setupPublication.version.toString()

    publishing.publications { publication ->
      publication.create("maven", MavenPublication::class.java) { pub ->
        pub.groupId = groupId
        pub.artifactId = artifactId
        pub.version = version

        if (isSpringBootApp()) {
          pub.artifact(tasks.named("bootJar"))
        } else {
          pub.from(components.getByName("kotlin"))
        }
      }
    }

    publishing.repositories { repoHandler ->
      repoHandler.maven { mavenRepo ->
        val repo = if (version.endsWith("-SNAPSHOT")) "maven-snapshots" else "maven-releases"
        mavenRepo.url = uri("https://nexus.craigmiller160.us/repository/$repo")
        mavenRepo.name = "CraigNexus"
        mavenRepo.credentials { creds ->
          creds.username = System.getenv("NEXUS_USER")
          creds.password = System.getenv("NEXUS_PASSWORD")
        }
      }
    }

    project.tasks.getByName("generateMetadataFileForMavenPublication") { metadataForPublicationTask
      ->
      project.tasks.findByName("bootJar")?.let { metadataForPublicationTask.dependsOn("bootJar") }
    }
  }
}
