package io.toolisticon.maven.model

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class MavenArtifactTest {


  @ParameterizedTest
  @CsvSource(
    value = [
      // valid long format
      "group,artifact,1,,group:artifact:1,false",
      // valid short format
      ",,,group:artifact:1,group:artifact:1,false",
      // invalid long format, version is missing
      "group,artifact,,,,true",
      // invalid short format, not a gav key
      ",,,g:a,,true",
      // invalid short format, not a gav key
      ",,,::,,true",
    ], nullValues = ["null",""]
  )
  fun `create artifact via flexible parameter`(
    groupId: GroupId?,
    artifactId: ArtifactId?,
    version: ArtifactVersion?,
    key: GavKey?,
    expectedKey: GavKey?,
    expectException: Boolean
  ) {
    fun create() = MavenArtifactParameter(groupId = groupId, artifactId = artifactId, version = version, gav = key)

    if (expectException) {
      assertThatThrownBy { create() }.isInstanceOf(IllegalArgumentException::class.java)
    } else {
      assertThat(create()().key()).isEqualTo(expectedKey)
    }
  }


  @Test
  internal fun `create parameter with groupId, artifactId and version`() {
    val param = MavenArtifactParameter(groupId = "g", artifactId = "a", version = "1")
    val artifact = param()

    assertThat(artifact).isEqualTo(MavenArtifact(groupId = "g", artifactId = "a", version = "1"))
    assertThat(artifact.key()).isEqualTo("g:a:1")
  }

  @Test
  internal fun `create parameter from key`() {
    val param = MavenArtifactParameter(gav = "g:a:1")
    val artifact = param()

    assertThat(artifact).isEqualTo(MavenArtifact(groupId = "g", artifactId = "a", version = "1"))
    assertThat(artifact.key()).isEqualTo("g:a:1")
  }
}
