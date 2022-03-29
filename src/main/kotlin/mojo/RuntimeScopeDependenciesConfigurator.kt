package io.toolisticon.maven.mojo

import io.toolisticon.maven.KotlinMojoHelper
import org.codehaus.plexus.classworlds.realm.ClassRealm
import org.codehaus.plexus.component.configurator.BasicComponentConfigurator
import org.codehaus.plexus.component.configurator.ComponentConfigurationException
import org.codehaus.plexus.component.configurator.ConfigurationListener
import org.codehaus.plexus.component.configurator.converters.composite.ObjectWithFieldsConverter
import org.codehaus.plexus.component.configurator.converters.special.ClassRealmConverter
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluationException
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluator
import org.codehaus.plexus.configuration.PlexusConfiguration
import java.io.File
import java.net.MalformedURLException
import java.net.URL

/**
 * Configured in `META-INFO/plexus/components.xml`.
 *
 * This configurator can load the runtime dependencies of the project it is running in and and the dependencies
 * to the scope of the plugin/mojo.
 *
 * @see <a href="https://stackoverflow.com/a/2659324/290425">stackoverflow.com/a/2659324/290425</a>
 */
class RuntimeScopeDependenciesConfigurator : BasicComponentConfigurator() {
  companion object {
    const val ROLE_HINT = "runtime-scope-dependencies-configurator"
  }

  private val logger = KotlinMojoHelper.logger()

  @Throws(ComponentConfigurationException::class)
  override fun configureComponent(
    component: Any, // the mojo
    configuration: PlexusConfiguration,
    expressionEvaluator: ExpressionEvaluator,
    containerRealm: ClassRealm,
    listener: ConfigurationListener
  ) {
    logger.info { "running configureComponent on ${component.javaClass.canonicalName}" }

    addProjectDependenciesToClassRealm(expressionEvaluator, containerRealm)

    converterLookup.registerConverter(ClassRealmConverter(containerRealm))

    ObjectWithFieldsConverter().processConfiguration(
      converterLookup, component, this::class.java.classLoader, configuration,
      expressionEvaluator, listener
    )
    //super.configureComponent(component, configuration, evaluator, realm, listener)
  }

  @Throws(ComponentConfigurationException::class)
  private fun addProjectDependenciesToClassRealm(expressionEvaluator: ExpressionEvaluator, containerRealm: ClassRealm) {
    val runtimeClasspathElements: List<String> = try {
      expressionEvaluator.evaluate("\${project.runtimeClasspathElements}") as List<String>
    } catch (e: ExpressionEvaluationException) {
      throw ComponentConfigurationException("There was a problem evaluating: \${project.runtimeClasspathElements}", e)
    }

    // Add the project dependencies to the ClassRealm
    buildURLs(runtimeClasspathElements).forEach { containerRealm.addURL(it) }
  }

  @Throws(ComponentConfigurationException::class)
  private fun buildURLs(runtimeClasspathElements: List<String>): Array<URL> {
    // Add the projects classes and dependencies
    val urls: MutableList<URL> = ArrayList(runtimeClasspathElements.size)
    for (element in runtimeClasspathElements) {
      try {
        val url: URL = File(element).toURI().toURL()
        urls.add(url)
        if (logger.isDebugEnabled) {
          logger.debug("Added to project class loader: $url")
        }
      } catch (e: MalformedURLException) {
        throw ComponentConfigurationException("Unable to access project dependency: $element", e)
      }
    }

    // Add the plugin's dependencies (so Trove stuff works if Trove isn't on
    return urls.toTypedArray()
  }

}
