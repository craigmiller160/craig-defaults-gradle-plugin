package io.craigmiller160.gradle.plugins.testutils

import io.kotest.matchers.collections.shouldContainInOrder
import io.kotest.matchers.collections.shouldHaveSize
import org.gradle.testkit.runner.BuildTask

fun List<BuildTask>.shouldHaveExecuted(vararg tasks: BuildTask) {
  val expectedTaskStrings = tasks.map { it.toString() }
  val actualTaskStrings = this.map { it.toString() }
  actualTaskStrings.shouldHaveSize(expectedTaskStrings.size)
  actualTaskStrings.shouldContainInOrder(expectedTaskStrings)
}
