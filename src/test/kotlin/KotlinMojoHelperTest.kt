package io.toolisticon.maven

import org.junit.jupiter.api.Test

internal class KotlinMojoHelperTest {

  @Test
  fun `read version from maven-properties`() {
    val properties = KotlinMojoHelper.mavenProperties

    println(properties.propertyNames())
  }
}
