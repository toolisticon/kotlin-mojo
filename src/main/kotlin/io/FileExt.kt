package io.toolisticon.maven.io

import java.io.File
import java.nio.file.Files

object FileExt {

  fun File.subFolder(name: String): File {
    createIfNotExists()

    return File("${this.path.removeSuffix(File.separator)}${File.separator}$name")
      .createIfNotExists()
  }

  fun File.createIfNotExists(): File {
    if (!this.exists()) {
      Files.createDirectories(this.toPath())
    }

    return this
  }
}
