package io.toolisticon.maven.plugin

import io.toolisticon.maven.AbstractMojoCommand
import io.toolisticon.maven.fn.FileExt.createIfNotExists
import io.toolisticon.maven.model.Configuration
import io.toolisticon.maven.mojo.MojoExecutorDsl.configuration
import io.toolisticon.maven.mojo.MojoExecutorDsl.plugin
import org.apache.maven.model.Plugin
import java.io.File

/**
 * MojoExecutor access to goals of [maven-resources-plugin](https://maven.apache.org/plugins/maven-resources-plugin/).
 */
object MavenResourcesPlugin : PluginWrapper {
  override val plugin: Plugin = plugin(artifactId = "maven-resources-plugin")

  /**
   * Wraps required parameters to execute [maven-resources-plugin/copy-resources](https://maven.apache.org/plugins/maven-resources-plugin/copy-resources-mojo.html).
   */
  data class CopyResourcesCommand(
    val outputDirectory: File,
    val resources: List<CopyResource>
  ) : AbstractMojoCommand(goal = GOAL, plugin = plugin) {
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

    data class CopyResource(
      val directory: File,
      val filtering: Boolean = false
    )
  }
}
