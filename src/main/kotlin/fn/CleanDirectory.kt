package io.toolisticon.maven.fn

import java.io.File
import java.nio.file.Files
import kotlin.io.path.deleteIfExists
import kotlin.io.path.isDirectory
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.name

data class CleanDirectory(
  val directory: File,
  val deleteFiles : Set<String>,
  val deleteEmptyDirectories: Boolean = false
) : Runnable {

  override fun run() {
    Files.walk(directory.toPath()).filter { it.name in deleteFiles }.forEach { it.deleteIfExists() }

    Files.walk(directory.toPath())
      .sorted(Comparator.reverseOrder())
      .filter { it.isDirectory() }.filter { it.listDirectoryEntries().isEmpty() }
      .forEach { it.deleteIfExists() }
  }
}
