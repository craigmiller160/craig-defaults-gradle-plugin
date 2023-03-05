val projectGroup: String by project
val projectVersion: String by project

plugins {
    kotlin("jvm")
    `maven-publish`
}

group = projectGroup
version = projectVersion
publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = rootProject.name
            version = project.version.toString()

            from(components["kotlin"])
        }
    }
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

dependencies {
    testImplementation(kotlin("test"))
    implementation(gradleApi())
}