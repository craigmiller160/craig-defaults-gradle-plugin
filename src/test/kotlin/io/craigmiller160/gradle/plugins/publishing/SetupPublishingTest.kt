package io.craigmiller160.gradle.plugins.publishing

import io.craigmiller160.gradle.plugins.testutils.GradleTestContext
import io.craigmiller160.gradle.plugins.testutils.GradleTestExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(GradleTestExtension::class)
class SetupPublishingTest {
  @Test
  fun `does not add publication when maven-publish task is not present`(
      context: GradleTestContext
  ) {
    TODO()
  }

  @Test
  fun `adds publication when maven-publish task is present`(context: GradleTestContext) {
    TODO()
  }

  @Test
  fun `fixes maven dependency management when generating pom_xml`(context: GradleTestContext) {
    TODO()
  }
}
