package io.toolisticon.maven

import io.toolisticon.maven.fn.FileExt.writeString
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

internal class KotlinMojoHelperTest {

  @TempDir
  private lateinit var tmp : File

  @Test
  fun `read version from maven-properties`() {
    val properties = KotlinMojoHelper.mavenProperties

    println(properties.propertyNames())
  }


  @Test
  fun `find all files in directory`() {
    tmp.resolve("foo.txt").writeString("foo")
    tmp.resolve("bar.xml").writeString("<bar />")

    println(KotlinMojoHelper.findIncludedFiles(tmp, includes = arrayOf("**/*.txt")))
  }
}
