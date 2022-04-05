package io.toolisticon.maven.mojo

import io.toolisticon.maven.mojo.MojoExecutorDsl.configuration
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


internal class MojoExecutorDslTest {

  @Test
  fun `from nested map`() {
    val map = mapOf(
      "foo" to "bar",
      "hello" to false
    )

    val c = configuration {
      element("nested") {
        map.forEach { (k, v) ->
          element(k, v)
        }
      }
    }

    assertThat(c.toString()).isEqualTo("""
      <?xml version="1.0" encoding="UTF-8"?>
      <configuration>
        <nested>
          <foo>bar</foo>
          <hello>false</hello>
        </nested>
      </configuration>
    """.trimIndent())
  }
}
