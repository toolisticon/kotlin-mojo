package io.toolisticon.maven.plugin

import io.toolisticon.maven.mojo.MojoExecutorDsl.gavKey
import io.toolisticon.maven.plugin.BuildHelperMavenPlugin.AddResourceDirectoryCommand
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

internal class BuildHelperMavenPluginTest {

  @TempDir
  private lateinit var tmpFile: File

  @Test
   fun `has correct gav`() {
    assertThat(BuildHelperMavenPlugin.plugin.gavKey()).isEqualTo("org.codehaus.mojo:build-helper-maven-plugin:3.4.0")
  }

  @Test
  fun `create command`() {
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
