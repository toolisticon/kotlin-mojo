package io.toolisticon.maven.model

import org.apache.maven.artifact.ArtifactUtils
import org.twdata.maven.mojoexecutor.MojoExecutor

interface MavenGav {

  val groupId: GroupId
  val artifactId: ArtifactId
  val version: ArtifactVersion

  fun key(): GavKey = ArtifactUtils.key(groupId, artifactId, version)
}

data class MavenArtifactParameter(
  val groupId: GroupId? = null,
  val artifactId: ArtifactId? = null,
  val version: ArtifactVersion? = null,
  val gav: GavKey? = null
) : () -> MavenArtifact {


  private val artifact: MavenArtifact

  init {
    val validLongFormat = (groupId != null && artifactId != null && version != null) && gav == null
    val validShortFormat = (groupId == null && artifactId == null && version == null) && gav != null && gav.contains(":")
    require(validLongFormat || validShortFormat) { "Either configure via groupId, artifactId and version OR the shorter key format (group:artifact:version)" }

    artifact = if (validLongFormat) {
      MavenArtifact(groupId = groupId!!, artifactId = artifactId!!, version = version!!)
    } else {
      val parts = gav!!.split(":")
      require(parts.size == 3) { "If the short format is used, the gav-key has to look like 'groupId:artifactId:version'" }
      require(parts.none { it.isBlank() }) { "gav key must not contain empty strings, use 'groupId:artifactId:version'" }

      MavenArtifact(groupId = parts[0], artifactId = parts[1], version = parts[2])
    }
  }

  override fun invoke(): MavenArtifact = artifact
}

data class MavenArtifact(
  override val groupId: GroupId,
  override val artifactId: ArtifactId,
  override val version: ArtifactVersion
) : MavenGav

fun MavenGav.dependency() = MojoExecutor.dependency(
  groupId,
  artifactId,
  version
)

fun MavenGav.plugin(dependencies: List<MavenArtifact> = emptyList()) = MojoExecutor.plugin(
  groupId,
  artifactId,
  version,
  dependencies.map { it.dependency() }
)
