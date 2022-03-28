package io.toolisticon.maven.logging

import org.apache.maven.plugin.logging.Log
import org.slf4j.helpers.MarkerIgnoringBase

/**
 * [org.slf4j.Logger] implementation forwarding to mavens [Log].
 *
 * Combined with [KLogger] this allows convenient kotlin logging in mojos.
 *
 * Since maven logging has no trace-level, tracing is implemented as debug level.
 */
class Slf4jMavenLogger(
  /**
   * The Mojo API states that the log should not be stored nor cached, so we provide a supplier function that gets
   * the core instances of [Log] every time.
   */
  private val logSupplier: () -> Log
) : MarkerIgnoringBase() {
  private val logger: Log get() = logSupplier()

  init {
    name = logger.javaClass.canonicalName
  }

  override fun isTraceEnabled() = logger.isDebugEnabled
  override fun trace(msg: String) = logger.debug(msg)
  override fun trace(format: String, arg: Any?) = trace(format(format, arg))
  override fun trace(format: String, arg1: Any?, arg2: Any?) = trace(format(format, arg1, arg2))
  override fun trace(format: String, vararg arguments: Any?) = trace(format(format, *arguments))
  override fun trace(msg: String, t: Throwable) = logger.debug(msg, t)

  override fun isDebugEnabled() = logger.isDebugEnabled
  override fun debug(msg: String) = logger.debug(msg)
  override fun debug(format: String, arg: Any?) = debug(format(format, arg))
  override fun debug(format: String, arg1: Any?, arg2: Any?) = debug(format(format, arg1, arg2))
  override fun debug(format: String, vararg arguments: Any?) = debug(format(format, *arguments))
  override fun debug(msg: String, t: Throwable) = logger.debug(msg, t)

  override fun isInfoEnabled() = logger.isInfoEnabled
  override fun info(msg: String) = logger.info(msg)
  override fun info(format: String, arg: Any?) = info(format(format, arg))
  override fun info(format: String, arg1: Any?, arg2: Any?) = info(format(format, arg1, arg2))
  override fun info(format: String, vararg arguments: Any?) = info(format(format, *arguments))
  override fun info(msg: String, t: Throwable) = logger.info(msg, t)

  override fun isWarnEnabled() = logger.isWarnEnabled
  override fun warn(msg: String) = logger.warn(msg)
  override fun warn(format: String, arg: Any?) = warn(format(format, arg))
  override fun warn(format: String, arg1: Any?, arg2: Any?) = warn(format(format, arg1, arg2))
  override fun warn(format: String, vararg arguments: Any?) = warn(format(format, *arguments))
  override fun warn(msg: String, t: Throwable) = logger.warn(msg, t)

  override fun isErrorEnabled() = logger.isErrorEnabled
  override fun error(msg: String) = logger.error(msg)
  override fun error(format: String, arg: Any?) = error(format(format, arg))
  override fun error(format: String, arg1: Any?, arg2: Any?) = error(format(format, arg1, arg2))
  override fun error(format: String, vararg arguments: Any?) = error(format(format, *arguments))
  override fun error(msg: String, t: Throwable) = logger.error(msg, t)

  private fun format(format: String, arg: Any?) = String.format(format, arg)
  private fun format(format: String, arg1: Any?, arg2: Any?) = String.format(format, arg1, arg2)
  private fun format(format: String, vararg arguments: Any?) = String.format(format, *arguments)
}
