package io.craigmiller160.gradle.plugins.publishing

import java.io.File
import org.gradle.api.Project
import org.gradle.api.publish.maven.tasks.GenerateMavenPom

private val REGEX =
    "(?s)(<dependencyManagement>.+?<dependencies>)(.+?)(</dependencies>.+?</dependencyManagement>)".toRegex()

fun Project.fixMavenDependencyManagement() {
  tasks.withType(GenerateMavenPom::class.java).all { generateMavenPom ->
    generateMavenPom.doLast {
      val pomXmlFile = File("$buildDir/publications/maven/pom-default.xml")
      val pomXmlText = pomXmlFile.readText()
      val updatedPomXmlText =
          REGEX.find(pomXmlText)?.let { matcher ->
            val firstDeps = matcher.groups[2]!!.value
            REGEX.replaceFirst(pomXmlText, "").let { REGEX.replaceFirst(it, "$1$2$firstDeps$3") }
          }
              ?: pomXmlText
      pomXmlFile.writeText(updatedPomXmlText)
    }
  }
}
