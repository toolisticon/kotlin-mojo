package io.toolisticon.maven.mojo

import io.toolisticon.maven.KotlinMojoHelper.versionlessKey
import io.toolisticon.maven.model.ArtifactId
import io.toolisticon.maven.model.GroupId
import io.toolisticon.maven.model.Scope
import io.toolisticon.maven.model.VersionlessKey
import io.toolisticon.maven.mojo.ArtifactPredicates.isAddedToClassPath
import io.toolisticon.maven.mojo.ArtifactPredicates.scopeIn
import io.toolisticon.maven.mojo.MavenExt.runtimeDependencyVersionlessKeys
import org.apache.maven.artifact.Artifact
import org.apache.maven.project.MavenProject
import java.util.function.Predicate

internal object ArtifactPredicates {
  fun scopeIn(scopes: Set<Scope>): Predicate<Artifact> = Predicate<Artifact> { scopes.contains(it.scope) }
  val isAddedToClassPath: Predicate<Artifact> = Predicate<Artifact> { it.artifactHandler.isAddedToClasspath }
}

class ArtifactScopePredicate(private val scopes: Set<Scope>) : Predicate<Artifact> {
  constructor(vararg scopes: Scope) : this(setOf(*scopes))

  override fun test(artifact: Artifact): Boolean = scopeIn(scopes).and(isAddedToClassPath).test(artifact)

  override fun toString() = "ArtifactScopePredicate(scopes=$scopes)"
}

class ProjectContainsRuntimeDependencyPredicate(
  private val versionlessKey: VersionlessKey
) : Predicate<MavenProject> {

  constructor(groupId: GroupId, artifactId: ArtifactId) : this(versionlessKey(groupId, artifactId))

  override fun test(project: MavenProject): Boolean = project.runtimeDependencyVersionlessKeys().contains(versionlessKey)

  override fun toString() = """ProjectContainsRuntimeDependencyPredicate(versionlessKey="$versionlessKey")"""
}
