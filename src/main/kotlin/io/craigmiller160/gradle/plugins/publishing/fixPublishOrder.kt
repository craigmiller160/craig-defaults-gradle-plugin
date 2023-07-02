package io.craigmiller160.gradle.plugins.publishing

import org.gradle.api.Project
import org.gradle.api.publish.maven.tasks.PublishToMavenRepository

fun Project.fixPublishOrder() {
//    tasks.withType(PublishToMavenRepository::class.java) {
//        it.dependsOn()
//    }
}