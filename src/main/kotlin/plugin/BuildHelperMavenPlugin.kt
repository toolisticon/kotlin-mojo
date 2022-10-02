package io.toolisticon.maven.plugin

import io.toolisticon.maven.AbstractMojoCommand
import io.toolisticon.maven.model.Configuration
import io.toolisticon.maven.mojo.MojoExecutorDsl
import io.toolisticon.maven.mojo.MojoExecutorDsl.configuration
import io.toolisticon.maven.mojo.MojoExecutorDsl.plugin
import org.apache.maven.model.Plugin
import java.io.File

object BuildHelperMavenPlugin : PluginWrapper {
  override val plugin: Plugin = plugin(groupId = "org.codehaus.mojo", artifactId = "build-helper-maven-plugin")

  /**
   * Wraps parameters for [build-helper-maven-plugin/add-resource](https://www.mojohaus.org/build-helper-maven-plugin/add-resource-mojo.html).
   */
  data class AddResourceDirectoryCommand(
    val resources: List<ResourceData>
  ) : AbstractMojoCommand(goal = GOAL, plugin = plugin) {
    companion object {
      val GOAL = MojoExecutorDsl.goal("add-resource")
    }

    constructor(resource: ResourceData) : this(listOf(resource))

    init {
      require(resources.isNotEmpty()) { "configure at least one additional resource." }
    }

    override val configuration: Configuration = configuration {
      element("resources") {
        resources.forEach {
          element("resource") {
            element("directory", it.directory)

            if (it.targetPath != null) {
              element("targetPath", it.targetPath)
            }
          }
        }
      }
    }
  }

  data class AddSourceDirectoryCommand(
    val sources: List<File>
  ) : AbstractMojoCommand(plugin = plugin, goal = GOAL) {
    companion object {
      val GOAL = MojoExecutorDsl.goal("add-source")
    }

    constructor(source: File ) : this(listOf(source))

    init {
      require(sources.isNotEmpty()) { "configure at least one additional source." }
    }

    override val configuration: Configuration = configuration {
      element("sources") {
        sources.forEach {
          element("source", it)
        }
      }
    }
  }
}
