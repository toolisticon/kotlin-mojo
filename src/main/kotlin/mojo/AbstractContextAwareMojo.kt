package io.toolisticon.maven.mojo

import io.toolisticon.maven.KotlinMojoHelper
import io.toolisticon.maven.MojoCommand
import io.toolisticon.maven.MojoCommandExecutor
import io.toolisticon.maven.MojoContext
import mu.KLogger
import org.apache.maven.execution.MavenSession
import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugin.BuildPluginManager
import org.apache.maven.plugins.annotations.Component
import org.apache.maven.plugins.annotations.Parameter
import org.apache.maven.project.MavenProject


/**
 * Root class for AbstractMojo. This gets the relevant components injected.
 *
 * * [MavenProject]?
 * * [MavenSession]
 * * [BuildPluginManager]
 *
 * Provides:
 *
 * * [KLogger] - using the maven [Log].
 * * [MojoComponents]
 */
abstract class AbstractContextAwareMojo : AbstractMojo(), MojoCommandExecutor {

  protected val logger: KLogger = KotlinMojoHelper.logger(this)

  @Parameter(defaultValue = "\${project}", readonly = true, required = false)
  private var project: MavenProject? = null

  @Parameter(defaultValue = "\${session}", readonly = true, required = true)
  private lateinit var session: MavenSession

  @Component
  private lateinit var buildPluginManager: BuildPluginManager

  val mojoContext by lazy {
    MojoContext(
      logger = logger,
      mavenProject = project,
      mavenSession = session,
      pluginManager = buildPluginManager
    )
  }

  override fun execute(command: MojoCommand) = mojoContext.execute(command)
}
