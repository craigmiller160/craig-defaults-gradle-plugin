val projectGroup: String by project
val projectVersion: String by project

plugins {
    kotlin("jvm") version "1.8.0"
    application
}

group = projectGroup
version = projectVersion

dependencies {
    testImplementation(kotlin("test"))
}