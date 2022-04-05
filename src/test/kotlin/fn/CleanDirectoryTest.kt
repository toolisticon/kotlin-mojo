package io.toolisticon.maven.fn

import io.toolisticon.maven.io.FileExt.subFolder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.io.PrintWriter
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile
import kotlin.io.path.name
import kotlin.streams.asSequence


internal class CleanDirectoryTest {

  @TempDir
  private lateinit var tmpDir: File

  @Test
  internal fun `delete marker files and delete empty directories`() {
    val remove = tmpDir.subFolder("a").subFolder("a1")
    val keep = tmpDir.subFolder("b").subFolder("b1")

    // create some files
    PrintWriter("${remove.path}/.gitkeep", Charsets.UTF_8).use { }
    PrintWriter("${keep.path}/.gitkeep", Charsets.UTF_8).use { }
    PrintWriter("${keep.path}/foo.txt", Charsets.UTF_8).use {
      it.write("bar")
    }

    CleanDirectory(directory = tmpDir, deleteFiles = setOf(".gitkeep"), deleteEmptyDirectories = true).run()

    val files: List<String> = filterDirectory { it.isRegularFile() }.map { it.name }
    assertThat(files).hasSize(1).containsExactly("foo.txt")

    val directories: List<Path> = filterDirectory { it.isDirectory() }
    assertThat(directories).hasSize(2)
  }

  private fun filterDirectory(filter: (Path) -> Boolean) = Files.walk(tmpDir.toPath())
    .asSequence()
    .filterNot { it.toFile() == tmpDir }
    .filter(filter)
    .toList()
}
