package io.toolisticon.maven.command

import io.toolisticon.maven.model.MavenArtifactParameter
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

internal class UnpackDependenciesCommandTest {

  @TempDir
  private lateinit var output: File

  @Test
  fun `create command`() {
    val parameter = MavenArtifactParameter(gav = "io.hello:world:1.0")
    val parameters = setOf(parameter)

    val cmd = UnpackDependenciesCommand(outputDirectory = output, artifactItems = parameters.map { it.toArtifactItem() }.toSet())

    Assertions.assertThat(cmd.configuration().toString()).isEqualTo("""
      <?xml version="1.0" encoding="UTF-8"?>
      <configuration>
        <outputDirectory>${output.path}</outputDirectory>
        <artifactItems>
          <artifactItem>
            <groupId>io.hello</groupId>
            <artifactId>world</artifactId>
            <version>1.0</version>
            <overWrite>false</overWrite>
          </artifactItem>
        </artifactItems>
      </configuration>
    """.trimIndent())
  }
}
