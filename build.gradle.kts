val projectGroup: String by project
val projectVersion: String by project

plugins {
    kotlin("jvm")
    `maven-publish`
    `java-gradle-plugin`
}

group = projectGroup
version = projectVersion
publishing {
    repositories {
        maven {
            val repo = if (project.version.toString().endsWith("-SNAPSHOT")) "maven-snapshots" else "maven-releases"
            url = uri("https://nexus-craigmiller160.ddns.net/repository/$repo")
            credentials {
                username = System.getenv("NEXUS_USER")
                password = System.getenv("NEXUS_PASSWORD")
            }
        }
    }
}

gradlePlugin {
    plugins {
        create("defaultsPlugin") {
            id = "io.craigmiller160.gradle.defaults"
            implementationClass = "io.craigmiller160.gradle.plugins.CraigDefaultsPlugin"
        }
    }
}