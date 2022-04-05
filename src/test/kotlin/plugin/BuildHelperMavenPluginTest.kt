package io.toolisticon.maven.plugin

import io.toolisticon.maven.plugin.BuildHelperMavenPlugin.AddResourceDirectoryCommand
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

internal class BuildHelperMavenPluginTest {

  @TempDir
  private lateinit var tmpFile: File

  @Test
  internal fun `create command`() {
    val resource = ResourceData(directory = tmpFile, targetPath = "/")

    val command = AddResourceDirectoryCommand(resource)

    assertThat(command.configuration.toString()).isEqualTo(
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <configuration>
        <resources>
          <resource>
            <directory>${resource.directory}</directory>
            <targetPath>${resource.targetPath}</targetPath>
          </resource>
        </resources>
      </configuration>
    """.trimIndent()
    )

    println(command)
  }

}
