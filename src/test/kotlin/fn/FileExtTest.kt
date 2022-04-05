package io.toolisticon.maven.fn

import io.toolisticon.maven.fn.FileExt.append
import io.toolisticon.maven.fn.FileExt.createSubFolder
import io.toolisticon.maven.fn.FileExt.createSubFolders
import io.toolisticon.maven.fn.FileExt.createSubFoldersFromPath
import io.toolisticon.maven.fn.FileExt.readString
import io.toolisticon.maven.fn.FileExt.removeRoot
import io.toolisticon.maven.fn.FileExt.writeString
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

internal class FileExtTest {

  @TempDir
  private lateinit var root: File

  @Test
  fun `create single subFolder`() {
    val foo = root.createSubFolder("foo")

    assertThat(foo).isDirectory
    assertThat(foo).exists()
  }

  @Test
  fun `create subFolders`() {
    // sub does not exist
    val sub = File(root.path + File.separator + "sub")
    assertThat(sub.exists()).isFalse

    val fooBar = sub.createSubFolders("foo", "bar")
    assertThat(sub.exists()).isTrue

    assertThat(fooBar).isDirectory
    assertThat(fooBar).exists()

    assertThat(fooBar.removeRoot(root)).isEqualTo("sub/foo/bar")
  }

  @Test
  fun `remove root from path`() {
    val foo = root.createSubFolder("foo")
    val fooPath: String = foo.removeRoot(root)

    assertThat(fooPath).isEqualTo("foo")
  }

  @Test
  fun `create subFolders from package`() {
    val packagePath = "hello.world"
    val packageDir = root.createSubFoldersFromPath(packagePath)

    assertThat(packageDir.removeRoot(root)).isEqualTo("hello/world")
  }

  @Test
  fun `create subFolders from path`() {
    val packagePath = "hello/world"
    val packageDir = root.createSubFoldersFromPath(packagePath)

    assertThat(root.append("hello")).exists().isDirectory
    assertThat(root.append("hello/world")).exists().isDirectory

    assertThat(packageDir.removeRoot(root)).isEqualTo("hello/world")
  }

  @Test
  fun `write string to file`() {
    val file = root.append("hello.txt")
    assertThat(file.exists()).isFalse
    assertThat(file.isDirectory).isFalse


    file.writeString("this is a test")
    assertThat(file.exists()).isTrue
    assertThat(file.isDirectory).isFalse

    assertThat(file.readString()).isEqualTo("this is a test")
  }

  @Test
  fun `write string to file in package path`() {
    val file = root.writeString("foo.bar", "hello.txt", "world")
    assertThat(file).exists()
    assertThat(file).isFile
    assertThat(file.removeRoot(root)).isEqualTo("foo/bar/hello.txt")
    assertThat(file.readString()).isEqualTo("world")
  }
}
