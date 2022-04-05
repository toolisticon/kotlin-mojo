package io.toolisticon.maven.plugin

import io.toolisticon.maven.MojoCommand
import io.toolisticon.maven.MojoCommand.Companion.toString
import io.toolisticon.maven.model.Configuration
import io.toolisticon.maven.model.Goal
import io.toolisticon.maven.mojo.MojoExecutorDsl
import io.toolisticon.maven.mojo.MojoExecutorDsl.configuration
import io.toolisticon.maven.mojo.MojoExecutorDsl.plugin
import org.apache.maven.model.Plugin
import org.apache.maven.model.Resource

object BuildHelperMavenPlugin : PluginWrapper {
  override val plugin: Plugin = plugin(groupId = "org.codehaus.mojo", artifactId = "build-helper-maven-plugin", version = "3.3.0")

  /**
   * Wraps parameters for [build-helper-maven-plugin/add-resource](https://www.mojohaus.org/build-helper-maven-plugin/add-resource-mojo.html).
   */
  data class AddResourceDirectoryCommand(
    val resources: List<ResourceData>
  ) : MojoCommand {
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

    override val plugin: Plugin = BuildHelperMavenPlugin.plugin
    override val goal: Goal = GOAL
    override fun toString() = toString(this)
  }
}
