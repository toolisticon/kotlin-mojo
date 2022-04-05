package io.toolisticon.maven.mojo

import io.toolisticon.maven.KotlinMojoHelper
import io.toolisticon.maven.model.ArtifactId
import io.toolisticon.maven.model.GroupId
import org.apache.maven.artifact.Artifact
import org.apache.maven.artifact.ArtifactUtils
import org.apache.maven.project.MavenProject

object MavenExt {

  /**
   * @return `true` if project has defined runtime dependency.
   */
  fun MavenProject.hasRuntimeDependency(groupId: GroupId, artifactId: ArtifactId) = KotlinMojoHelper.Predicates.hasRuntimeDependency(groupId, artifactId).test(this)

  fun MavenProject.runtimeDependencyVersionlessKeys() = this.artifacts
    .filter(ArtifactScopePredicate(Artifact.SCOPE_COMPILE, Artifact.SCOPE_RUNTIME)::test)
    .map(ArtifactUtils::versionlessKey)
    .toSortedSet()

}
