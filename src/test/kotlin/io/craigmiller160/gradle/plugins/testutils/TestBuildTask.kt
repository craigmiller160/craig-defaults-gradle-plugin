package io.craigmiller160.gradle.plugins.testutils

import org.gradle.testkit.runner.BuildTask
import org.gradle.testkit.runner.TaskOutcome

data class TestBuildTask(
    val taskPath: String,
    val taskOutcome: TaskOutcome
) : BuildTask {
    override fun getPath(): String = taskPath
    override fun getOutcome(): TaskOutcome = taskOutcome
}
