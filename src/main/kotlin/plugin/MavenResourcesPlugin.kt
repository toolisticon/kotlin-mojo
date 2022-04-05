package io.toolisticon.maven.plugin

import io.toolisticon.maven.KotlinMojoHelper.MAVEN_PLUGINS_GROUP_ID
import io.toolisticon.maven.MojoCommand
import io.toolisticon.maven.MojoCommand.Companion.toString
import io.toolisticon.maven.fn.FileExt.createIfNotExists
import io.toolisticon.maven.model.Configuration
import io.toolisticon.maven.model.Goal
import io.toolisticon.maven.mojo.MojoExecutorDsl
import io.toolisticon.maven.mojo.MojoExecutorDsl.configuration
import io.toolisticon.maven.mojo.MojoExecutorDsl.plugin
import org.apache.maven.model.Plugin
import java.io.File

object MavenResourcesPlugin : PluginWrapper {
  override val plugin: Plugin = plugin(
    MAVEN_PLUGINS_GROUP_ID,
    "maven-resources-plugin",
    "3.0.2"
  )

  /**
   * Wraps required parameters to execute [maven-resources-plugin/copy-resources](https://maven.apache.org/plugins/maven-resources-plugin/copy-resources-mojo.html).
   */
  data class CopyResourcesCommand(
    val outputDirectory: File,
    val resources: List<CopyResource>
  ) : MojoCommand {
    companion object {
      const val GOAL = "copy-resources"
    }

    init {
      require(resources.isNotEmpty()) { "at least one resource to copy has to be configured" }
    }

    override val configuration: Configuration = configuration {
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

    override val plugin: Plugin = MavenResourcesPlugin.plugin
    override val goal: Goal = MojoExecutorDsl.goal(GOAL)
    override fun toString() = toString(this)

    data class CopyResource(
      val directory: File,
      val filtering: Boolean = false
    )
  }
}
