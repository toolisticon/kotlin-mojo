package io.toolisticon.maven.io

import java.io.File
import java.nio.file.Files

/**
 * Extension functions for java io to create directories during maven build.
 */
object FileExt {

  /**
   * Creates subfolder with `name` and ensures the path exists.
   */
  fun File.subFolder(name: String): File {
    createIfNotExists()

    return File("${this.path.removeSuffix(File.separator)}${File.separator}$name")
      .createIfNotExists()
  }

  /**
   * Creates the file (and its path) if it does not exist yet.
   */
  fun File.createIfNotExists(): File {
    if (!this.exists()) {
      Files.createDirectories(this.toPath())
    }

    return this
  }
}
