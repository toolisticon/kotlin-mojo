package io.toolisticon.maven

import io.toolisticon.maven.model.ArtifactId
import io.toolisticon.maven.model.ArtifactVersion
import io.toolisticon.maven.model.GroupId
import io.toolisticon.maven.model.Scope
import org.apache.maven.artifact.Artifact
import org.apache.maven.artifact.DefaultArtifact
import org.apache.maven.artifact.handler.DefaultArtifactHandler

object KotlinMojoTestHelper {

  class ArtifactFake(
    groupId: GroupId,
    artifactId: ArtifactId,
    version: ArtifactVersion,
    scope: Scope = Artifact.SCOPE_COMPILE,
    type: String = "jar",
    addedToClassPath: Boolean = false
  ) : DefaultArtifact(groupId, artifactId, version, scope, type, null, DefaultArtifactHandler().apply {
    isAddedToClasspath = addedToClassPath
  })

}
