package io.toolisticon.maven.plugin

import io.toolisticon.maven.KotlinMojoHelper.MAVEN_PLUGINS_GROUP_ID
import io.toolisticon.maven.MojoCommand
import io.toolisticon.maven.MojoCommand.Companion.toString
import io.toolisticon.maven.fn.FileExt.createIfNotExists
import io.toolisticon.maven.model.Configuration
import io.toolisticon.maven.model.Goal
import io.toolisticon.maven.model.MavenArtifactParameter
import io.toolisticon.maven.mojo.MojoExecutorDsl
import io.toolisticon.maven.mojo.MojoExecutorDsl.configuration
import io.toolisticon.maven.mojo.MojoExecutorDsl.plugin
import org.apache.maven.model.Plugin
import java.io.File

object MavenDependencyPlugin : PluginWrapper {

  override val plugin: Plugin = plugin(MAVEN_PLUGINS_GROUP_ID, "maven-dependency-plugin", "3.3.0")

  /**
   * Wraps required parameters to execute [maven-dependency-plugin/unpack](https://maven.apache.org/plugins/maven-dependency-plugin/unpack-mojo.html).
   */
  data class UnpackDependenciesCommand(
    val outputDirectory: File,
    val artifactItems: Set<ArtifactItem>,
    val excludes: String? = null
  ) : MojoCommand {
    companion object {
      val GOAL: Goal = MojoExecutorDsl.goal("unpack")

      fun MavenArtifactParameter.toArtifactItem() = this().let {
        ArtifactItem(groupId = it.groupId, artifactId = it.artifactId, version = it.version)
      }
    }

    override val configuration: Configuration = configuration {
      element("outputDirectory", outputDirectory.createIfNotExists())
      element("artifactItems") {
        artifactItems.forEach {
          element("artifactItem") {
            element("groupId", it.groupId)
            element("artifactId", it.artifactId)
            element("version", it.version)
            element("overWrite", it.overwrite)
          }
        }
      }

      if (excludes != null) {
        element("excludes", excludes)
      }
    }

    override val goal: Goal = GOAL
    override val plugin: Plugin = MavenDependencyPlugin.plugin
    override fun toString() = toString(this)

    data class ArtifactItem(
      val groupId: String,
      val artifactId: String,
      val version: String,
      val overwrite: Boolean = false
    )
  }


//
//  private lateinit var _outputDirectory: File
//  private lateinit var schemaArtifacts: ArtifactItems
//  private lateinit var includeSchemas: Set<String>
//
//
//  fun outputDirectory(outputDirectory: File) = apply {
//    this._outputDirectory = outputDirectory
//  }
//
//  fun schemaArtifacts(schemaArtifacts: Set<String>) = apply {
//    this.schemaArtifacts = schemaArtifacts.fold(ArtifactItems()) { items, gav ->
//      val (groupId, artifactId, version) = gav.trim().split(":")
//      items.add(ArtifactItem(groupId, artifactId, version))
//      items
//    }
//  }
//
//  fun includeSchemas(includeSchemas: Set<String>) = apply {
//    this.includeSchemas = includeSchemas.map { it.trim() }
//      .map { it.removeSuffix(".avsc") }
//      .map { it.replace(".", "/") }
//      .map { it.plus(".avsc") }
//      .toSortedSet()
//  }
//
//
//  private fun elementArtifactItems(): Element = schemaArtifacts.element()
//
//  private fun elementIncludeSchemas(): Element = element("includes", includeSchemas.joinToString(","))
//
//  override fun run() = executeMojo(
//    GOAL,
//    element(name("outputDirectory"), _outputDirectory.path),
//    elementArtifactItems(),
//    elementIncludeSchemas(),
//    element("excludes", "META-INF/**")
//  )
//

}
