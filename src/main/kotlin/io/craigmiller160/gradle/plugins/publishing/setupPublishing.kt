package io.craigmiller160.gradle.plugins.publishing

import org.gradle.api.Project

fun Project.setupPublishing() {
    pluginManager.withPlugin("maven-publish") {
        println("WORKING")
    }
//    println("PLUGIN: ${plugins.findPlugin("org.gradle.maven-publish")}")
}