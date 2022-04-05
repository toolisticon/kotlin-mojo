package io.toolisticon.maven.fn

import java.io.File
import java.io.PrintWriter
import java.nio.file.Files

/**
 * Extension functions for java io to work with files during maven build.
 */
object FileExt {

  fun File.removeRoot(root: File): String = this.absolutePath.removePrefix(root.absolutePath)
    .removePrefix(File.separator)

  /**
   * Creates subfolder with `name` and ensures the path exists.
   */
  fun File.createSubFolder(name: String): File {
    createIfNotExists()

    return File("${this.path.removeSuffix(File.separator)}${File.separator}$name")
      .createIfNotExists()
  }

  /**
   * Creates a subFolder hierarchy from given names.
   */
  fun File.createSubFolders(vararg names: String) = names.fold(this.createIfNotExists()) { file, name -> file.createSubFolder(name) }


  /**
   * Create a subFolder structure under file.
   * Sub directory path can be specified as package (`this.is.awesome`) or path (`ths/is/awesome`).
   */
  fun File.createSubFoldersFromPath(path: String): File {
    val names: Array<String> = path.split(".").toTypedArray()
    return createSubFolders(*names)
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

  /**
   * New file `$path/name`. File is not being created!
   */
  fun File.append(name: String) = File(this.path + File.separator + name)

  /**
   * Read file content as string.
   */
  fun File.readString(): String = Files.readString(this.toPath(), Charsets.UTF_8)

  fun File.writeString(value: String): File {
    require(!this.isDirectory) { "cannot write string to directory." }

    PrintWriter(this, Charsets.UTF_8).use {
      it.write(value)
    }

    return this
  }

  fun File.writeString(path: String, fileName: String, content: String) = this.createSubFoldersFromPath(path)
    .append(fileName)
    .writeString(content)


//  fun createJavaFile(target:File, fqn: String, code:String): File {
//    val tmpFqn = fqn.removeSuffix(".java")
//
//    val classFile = tmpFqn.substringAfterLast(".") + ".java"
//    val packagePath = tmpFqn.substringBeforeLast(".")
//
//    val directory = createFqnPath(target, packagePath)
//
//    val file = File("${directory.path}/$classFile")
//
//    PrintWriter(file).use {
//      it.write(TestFixtures.generatedBankAccountCreatedEvent_java)
//    }
//
//    return file
//  }
}
