package io.toolisticon.maven.command

import io.toolisticon.maven.KotlinMojoHelper.MAVEN_PLUGINS_GROUP_ID
import io.toolisticon.maven.MojoCommand
import io.toolisticon.maven.io.FileExt.createIfNotExists
import io.toolisticon.maven.model.Configuration
import io.toolisticon.maven.model.Goal
import io.toolisticon.maven.model.MavenArtifactParameter
import io.toolisticon.maven.mojo.MojoExecutorDsl.configuration
import io.toolisticon.maven.mojo.MojoExecutorDsl.goal
import io.toolisticon.maven.mojo.MojoExecutorDsl.plugin
import org.apache.maven.model.Plugin
import java.io.File

data class UnpackDependenciesCommand(
  val outputDirectory: File,
  val artifactItems: Set<ArtifactItem>,
  val excludes: String? = null
) : MojoCommand {
  companion object {
    val PLUGIN = plugin(MAVEN_PLUGINS_GROUP_ID, "maven-dependency-plugin", "3.3.0")
    val GOAL: Goal = goal("unpack")
  }

  override fun configuration(): Configuration = configuration {
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
  }

  override val goal: Goal = "unpack"
  override val plugin: Plugin = PLUGIN

  data class ArtifactItem(
    val groupId: String,
    val artifactId: String,
    val version: String,
    val overwrite: Boolean = false
  )
}

fun MavenArtifactParameter.toArtifactItem() = this().let {
  UnpackDependenciesCommand.ArtifactItem(groupId = it.groupId, artifactId = it.artifactId, version = it.version)
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
