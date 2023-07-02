package io.craigmiller160.gradle.plugins.testutils

import org.gradle.testkit.runner.BuildTask
import org.gradle.testkit.runner.TaskOutcome

data class TestBuildTask(
    val path: String,
    val outcome: TaskOutcome
) : BuildTask {
    override fun getPath(): String = path
    override fun getOutcome(): TaskOutcome = outcome
}
