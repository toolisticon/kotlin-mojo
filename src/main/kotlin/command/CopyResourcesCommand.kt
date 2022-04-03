package io.toolisticon.maven.command

import io.toolisticon.maven.KotlinMojoHelper.MAVEN_PLUGINS_GROUP_ID
import io.toolisticon.maven.MojoCommand
import io.toolisticon.maven.io.FileExt.createIfNotExists
import io.toolisticon.maven.model.Configuration
import io.toolisticon.maven.model.Goal
import io.toolisticon.maven.mojo.MojoExecutorDsl.configuration
import io.toolisticon.maven.mojo.MojoExecutorDsl.gavKey
import io.toolisticon.maven.mojo.MojoExecutorDsl.goal
import io.toolisticon.maven.mojo.MojoExecutorDsl.plugin
import org.apache.maven.model.Plugin
import java.io.File

/**
 * Wraps required parameters to execute [maven-resources-plugin/copy-resources](https://maven.apache.org/plugins/maven-resources-plugin/copy-resources-mojo.html).
 */
data class CopyResourcesCommand(
  val outputDirectory: File,
  val resources: List<CopyResource>
) : MojoCommand {
  companion object {
    val PLUGIN = plugin(MAVEN_PLUGINS_GROUP_ID, "maven-resources-plugin", "3.0.2")
    const val GOAL = "copy-resources"
  }

  init {
    require(resources.isNotEmpty()) { "at least one resource to copy has to be configured" }
  }

  override fun configuration(): Configuration = configuration {
    element("outputDirectory", outputDirectory.createIfNotExists())
    element("resources") {
      resources.forEach {
        element("resource") {
          element("directory", it.directory)
          element("filtering", it.filtering)
        }
      }
    }
  }

  override fun toString() = "CopyResourcesCommand(" +
    "plugin=${plugin.gavKey()}, " +
    "goal='$goal', " +
    "outputDirectory=$outputDirectory, " +
    "resources=$resources)"

  override val goal: Goal = goal(GOAL)
  override val plugin: Plugin = PLUGIN

  data class CopyResource(
    val directory: File,
    val filtering: Boolean = false
  )
}
