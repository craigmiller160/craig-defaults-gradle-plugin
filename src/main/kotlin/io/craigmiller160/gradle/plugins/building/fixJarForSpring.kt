package io.craigmiller160.gradle.plugins.building

import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar

fun Project.fixJarForSpring() {
    afterEvaluate { project ->
        project.tasks.findByName("bootJar")
            ?.let {
                project.tasks.getByName("jar") {
                    val task = it as Jar
                    task.archiveClassifier.set("foo")
                }
            }
    }
}