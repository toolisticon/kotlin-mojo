package io.toolisticon.maven

import io.toolisticon.maven.model.Configuration
import io.toolisticon.maven.model.Goal
import io.toolisticon.maven.mojo.MojoExecutorDsl
import io.toolisticon.maven.mojo.MojoExecutorDsl.gavKey
import org.apache.maven.model.Plugin
import org.twdata.maven.mojoexecutor.MojoExecutor

/**
 * Command wrapping all required data to execute a 3rd party plugin using the [MojoExecutor]. The [MojoExecutor.ExecutionEnvironment]
 * is not included, this is part of the [MojoCommandExecutor].
 */
interface MojoCommand {
  companion object {

    /**
     * Generic toString implementation based on interface vals.
     */
    fun toString(command: MojoCommand, name: String? = null) = with(command) {
      "${name ?: this::class.simpleName ?: MojoCommand::class.simpleName}(" +
        "plugin='${plugin.gavKey()}', " +
        "goal='${goal}', " +
        "configuration=${configuration.toString().replace("\n", "")}" +
        ")"
    }

    /**
     * Create anonymous implementation of [MojoCommand].
     */
    fun createMojoCommand(
      name: String? = null,
      plugin: Plugin,
      goal: Goal,
      receiver: MojoExecutorDsl.ElementListDsl.() -> Unit
    ): MojoCommand = object : MojoCommand {
      override val plugin: Plugin = plugin
      override val goal: Goal = goal
      override val configuration: Configuration = MojoExecutor.configuration(*MojoExecutorDsl.receiverToArray(receiver))

      override fun toString() = Companion.toString(this, name)
    }
  }

  /**
   * The plugin to use.
   */
  val plugin: Plugin

  /**
   * The goal to execute.
   */
  val goal: Goal

  /**
   * The configuration to pass.
   */
  val configuration: Configuration
}
