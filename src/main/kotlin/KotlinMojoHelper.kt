@file:Suppress("unused")

package io.toolisticon.maven

import io.toolisticon.maven.logging.Slf4jMavenLogger
import io.toolisticon.maven.model.ArtifactId
import io.toolisticon.maven.model.ArtifactVersion
import io.toolisticon.maven.model.GroupId
import io.toolisticon.maven.model.VersionlessKey
import io.toolisticon.maven.mojo.ProjectContainsRuntimeDependencyPredicate
import mu.KLogger
import mu.toKLogger
import org.apache.maven.artifact.ArtifactUtils
import org.apache.maven.plugin.Mojo
import org.apache.maven.plugin.logging.Log
import org.apache.maven.plugin.logging.SystemStreamLog
import org.apache.maven.shared.model.fileset.FileSet
import org.apache.maven.shared.model.fileset.util.FileSetManager
import java.io.File
import java.util.*

/**
 * Root class providing simplified access to most common use cases.
 */
object KotlinMojoHelper {
  const val BUILD_PROPERTIES = "kotlin-mojo-helper-maven.properties"
  const val MAVEN_PLUGINS_GROUP_ID: GroupId = "org.apache.maven.plugins"

  /**
   * @param logSupplier function providing a [Log].
   * @return [KLogger] wrapping [Log]
   */
  fun logger(logSupplier: () -> Log): KLogger = Slf4jMavenLogger(logSupplier).toKLogger()

  /**
   * @param mojo the mojo providing the log.
   * @see logger
   * @return [KLogger] wrapping [Log]
   */
  fun logger(mojo: Mojo): KLogger = logger(mojo::getLog)

  /**
   * @param log the log to wrap, defaults to [SystemStreamLog]
   * @return [KLogger] wrapping [Log], using [SystemStreamLog] as default, primarily for testing.
   */
  fun logger(log: Log = SystemStreamLog()) = logger { log }

  fun versionlessKey(groupId: GroupId /* = kotlin.String */, artifactId: ArtifactId /* = kotlin.String */): VersionlessKey =
    ArtifactUtils.versionlessKey(groupId, artifactId)

  /**
   * Access to predicates on maven types.
   */
  object Predicates {

    /**
     * @return if the project in which the plugin is used has a dependency available on the runtime classpath
     */
    fun hasRuntimeDependency(groupId: GroupId /* = kotlin.String */, artifactId: ArtifactId /* = kotlin.String */) =
      ProjectContainsRuntimeDependencyPredicate(groupId, artifactId)

  }

  internal fun ArtifactId.versionFromProperties(): ArtifactVersion = mavenProperties.getProperty("plugin-wrapper.${this}.version")

  internal val mavenProperties: Properties by lazy {
    val properties = Properties()
    KotlinMojoHelper::class.java.getResourceAsStream("/$BUILD_PROPERTIES")?.also { properties.load(it) }

    properties
  }

  fun findIncludedFiles(directory: File, includes: Array<String> = arrayOf("**/*"), excludes: Array<String> = emptyArray()): List<String> = findIncludedFiles(
    absPath = directory.absolutePath, includes = includes, excludes = excludes
  )

  fun findIncludedFiles(absPath: String, includes: Array<String> = arrayOf("**/*"), excludes: Array<String> = emptyArray()): List<String> {
    val fileSetManager = FileSetManager()
    val fs = FileSet().apply {
      directory = absPath
      isFollowSymlinks = false
    }

    for (include in includes) {
      fs.addInclude(include)
    }
    for (exclude in excludes) {
      fs.addExclude(exclude)
    }
    return fileSetManager.getIncludedFiles(fs)
      .filterNotNull()
  }
}
