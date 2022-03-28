@file:Suppress("unused")
package io.toolisticon.maven

import io.toolisticon.maven.logging.Slf4jMavenLogger
import mu.KLogger
import mu.toKLogger
import org.apache.maven.plugin.Mojo
import org.apache.maven.plugin.logging.Log
import org.apache.maven.plugin.logging.SystemStreamLog

object KotlinMojoHelper {

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
  fun logger(mojo: Mojo) : KLogger = logger(mojo::getLog)

  /**
   * @param log the log to wrap, defaults to [SystemStreamLog]
   * @return [KLogger] wrapping [Log], using [SystemStreamLog] as default, primarily for testing.
   */
  fun logger(log: Log = SystemStreamLog()) = logger{log}
}
