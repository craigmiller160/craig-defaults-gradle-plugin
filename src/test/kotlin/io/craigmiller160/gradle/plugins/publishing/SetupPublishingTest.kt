package io.craigmiller160.gradle.plugins.publishing

import io.craigmiller160.gradle.plugins.testutils.GradleTestContext
import io.craigmiller160.gradle.plugins.testutils.GradleTestExtension
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import java.nio.file.Paths
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory
import kotlin.io.path.exists
import kotlin.io.path.readText
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.w3c.dom.NodeList

@ExtendWith(GradleTestExtension::class)
class SetupPublishingTest {
  @Test
  fun `does not add publication when maven-publish task is not present`(
      context: GradleTestContext
  ) {
    val script =
        """
          plugins {
            id("io.craigmiller160.gradle.defaults") version "${context.pluginVersion}"
            kotlin("jvm") version "1.8.20"
          }
      """
            .trimIndent()
    context.writeBuildScript(script)

    val result = context.runner.withArguments("tasks").build()
    result.output.shouldNotContain("publishMavenPublicationToCraigNexusRepository")
  }

  @Test
  fun `adds publication when maven-publish task is present`(context: GradleTestContext) {
    val script =
        """
          plugins {
            id("io.craigmiller160.gradle.defaults") version "${context.pluginVersion}"
            kotlin("jvm") version "1.8.20"
            `maven-publish`
          }
      """
            .trimIndent()
    context.writeBuildScript(script)

    val result = context.runner.withArguments("tasks").build()
    result.output.shouldContain("publishMavenPublicationToCraigNexusRepository")
  }

  @Test
  fun `runs publish task and fixes pom_xml`(context: GradleTestContext) {
    val group = "io.craigmiller160.test"
    val version = "1.0.0"
    val script =
        """
          plugins {
            id("io.craigmiller160.gradle.defaults") version "${context.pluginVersion}"
            kotlin("jvm") version "1.8.20"
            `maven-publish`
            id("org.springframework.boot") version "3.0.4"
            id("io.spring.dependency-management") version "1.1.0"
          }
          
          dependencyManagement {
            imports {
              mavenBom("org.springdoc:springdoc-openapi:2.0.3")
            }
          }
          
          group = "$group"
          version = "$version"
      """
            .trimIndent()
    context.writeBuildScript(script)

    val result = context.runner.withArguments("publishToMavenLocal").build()
    val pomPath =
        Paths.get(
            System.getProperty("user.home"),
            ".m2",
            "repository",
            *group.split(".").toTypedArray(),
            GradleTestExtension.PROJECT_NAME,
            version,
            "${GradleTestExtension.PROJECT_NAME}-$version.pom")
    assertTrue { pomPath.exists() }
    val xml = pomPath.readText()
    println(xml)

    val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xml)
    val xPath = XPathFactory.newInstance().newXPath()
    val nodes =
        xPath.compile("/project/dependencyManagement").evaluate(doc, XPathConstants.NODESET)
            as NodeList
    assertEquals(1, nodes.length)
  }
}
