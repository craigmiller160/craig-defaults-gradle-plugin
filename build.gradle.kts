val projectGroup: String by project
val projectVersion: String by project

plugins {
    kotlin("jvm")
}

group = projectGroup
version = projectVersion

dependencies {
    testImplementation(kotlin("test"))
    implementation(gradleApi())
}