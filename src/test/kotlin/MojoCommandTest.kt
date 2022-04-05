package io.toolisticon.maven

import io.toolisticon.maven.MojoCommand.Companion.createMojoCommand
import io.toolisticon.maven.mojo.MojoExecutorDsl.plugin
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MojoCommandTest {

  @Test
  fun `create generic mojo command`() {
    val cmd = createMojoCommand(
      plugin = plugin("group", "plugin", "version"),
      goal = "goal"
    ) {
      element("foo", "bar")
    }

    assertThat(cmd.toString()).isEqualTo("""MojoCommand(plugin='group:plugin:version', goal='goal', configuration=<?xml version="1.0" encoding="UTF-8"?><configuration>  <foo>bar</foo></configuration>)""")
  }

  @Test
  fun `create generic named mojo command`() {
    val cmd = createMojoCommand(
      name = "FooCommand",
      plugin = plugin("group", "plugin", "version"),
      goal = "goal"
    ) {
      element("foo", "bar")
    }

    assertThat(cmd.toString()).isEqualTo("""FooCommand(plugin='group:plugin:version', goal='goal', configuration=<?xml version="1.0" encoding="UTF-8"?><configuration>  <foo>bar</foo></configuration>)""")
  }
}
