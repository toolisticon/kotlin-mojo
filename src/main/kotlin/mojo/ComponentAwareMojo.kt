package io.toolisticon.maven.mojo

import io.toolisticon.maven.KotlinMojoHelper
import mu.KLogger
import org.apache.maven.execution.MavenSession
import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugin.BuildPluginManager
import org.apache.maven.plugins.annotations.Component
import org.apache.maven.plugins.annotations.Parameter
import org.apache.maven.project.MavenProject
import org.twdata.maven.mojoexecutor.MojoExecutor
import kotlin.streams.asSequence


data class MojoComponents(
  val logger: KLogger,
  val project: MavenProject,
  val session: MavenSession,
  val buildPluginManager: BuildPluginManager
) {

  val environment: MojoExecutor.ExecutionEnvironment by lazy {
    MojoExecutor.executionEnvironment(project, session, buildPluginManager)
  }

  val classpathElements: Set<String> by lazy {
    project
      .compileClasspathElements
      .stream()
      .asSequence()
      .filterNot { project.build.outputDirectory == it } // TODO copied from camunda-swagger, why is that?
      .sortedBy { it.substringAfterLast("/") }
      .toSet()
  }

  override fun toString() = """
    MojoContext(
        logger=${logger.name},
        project=${project.groupId}.${project.artifactId},
        session=$session,
        buildPluginManager=$buildPluginManager,
        classpathElements=$classpathElements
  """.trimIndent()

}


/**
 * Root class for AbstractMojo. This gets the relevant components injected.
 *
 * * [MavenProject]
 * * [MavenSession]
 * * [BuildPluginManager]
 *
 * Provides:
 *
 * * [KLogger] - using the maven [Log].
 * * [MojoComponents]
 */
abstract class ComponentAwareMojo : AbstractMojo() {

  protected val logger: KLogger = KotlinMojoHelper.logger(this)

  @Parameter(defaultValue = "\${project}", readonly = true)
  private lateinit var project: MavenProject

  @Parameter(defaultValue = "\${session}", readonly = true)
  private lateinit var session: MavenSession

  @Component
  private lateinit var buildPluginManager: BuildPluginManager

  /**
   * Wrapped components, available to all implementing mojos.
   */
  val components by lazy {
    MojoComponents(
      logger = logger,
      project = project,
      session = session,
      buildPluginManager = buildPluginManager
    )
  }
}
