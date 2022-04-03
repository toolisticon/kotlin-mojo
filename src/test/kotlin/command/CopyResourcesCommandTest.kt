package io.toolisticon.maven.command

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

internal class CopyResourcesCommandTest {

  @TempDir
  private lateinit var resourceDir: File

  @TempDir
  private lateinit var outputDir: File

  @Test
  internal fun `create command`() {
    val resource = CopyResourcesCommand.CopyResource(directory = resourceDir)

    val cmd = CopyResourcesCommand(outputDirectory = outputDir, resources = listOf(resource))

    assertThat(cmd.configuration().toString()).isEqualTo("""
      <?xml version="1.0" encoding="UTF-8"?>
      <configuration>
        <outputDirectory>${outputDir.path}</outputDirectory>
        <resources>
          <resource>
            <directory>${resourceDir.path}</directory>
            <filtering>false</filtering>
          </resource>
        </resources>
      </configuration>
    """.trimIndent())
  }


}
