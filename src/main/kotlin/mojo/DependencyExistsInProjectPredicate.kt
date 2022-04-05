package io.toolisticon.maven.mojo

import io.toolisticon.maven.model.ArtifactId
import io.toolisticon.maven.model.GroupId
import io.toolisticon.maven.model.VersionlessKey
import org.apache.maven.artifact.ArtifactUtils
import org.apache.maven.project.MavenProject
import java.util.function.Predicate

data class DependencyExistsInProjectPredicate(
  val versionLessKey: VersionlessKey
) : Predicate<MavenProject> {

  constructor(groupId: GroupId, artifactId: ArtifactId) : this(ArtifactUtils.versionlessKey(groupId, artifactId))

  override fun test(project: MavenProject): Boolean = project.artifactMap.containsKey(versionLessKey)

}

