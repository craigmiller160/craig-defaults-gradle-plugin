import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val projectGroup: String by project
val projectVersion: String by project

plugins {
    kotlin("jvm")
    `maven-publish`
    `java-gradle-plugin`
    id("com.diffplug.spotless") version "6.17.0"
}

group = projectGroup
version = projectVersion
java.sourceCompatibility = JavaVersion.VERSION_19
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

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "19"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

gradlePlugin {
    plugins {
        create("defaultsPlugin") {
            id = "io.craigmiller160.gradle.defaults"
            implementationClass = "io.craigmiller160.gradle.plugins.CraigDefaultsPlugin"
        }
    }
}

spotless {
    kotlin {
        ktfmt()
    }
}