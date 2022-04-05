package io.toolisticon.maven.mojo

import io.toolisticon.maven.KotlinMojoHelper
import io.toolisticon.maven.KotlinMojoTestHelper.ArtifactFake
import org.apache.maven.project.MavenProject
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

internal class MavenPredicatesTest {
  private val project = mock<MavenProject>()

  @Test
  fun `has runtime dependency`() {
    val predicate = KotlinMojoHelper.Predicates.hasRuntimeDependency("foo", "bar")

    whenever(project.artifacts).thenReturn(setOf())
    assertThat(predicate.test(project)).isFalse

    whenever(project.artifacts).thenReturn(setOf(ArtifactFake("foo", "bar", "1.0")))
    assertThat(predicate.test(project)).isFalse

    whenever(project.artifacts).thenReturn(setOf(ArtifactFake("foo", "bar", "1.0", addedToClassPath = true)))
    assertThat(predicate.test(project)).isTrue

  }
}
