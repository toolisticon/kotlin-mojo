package io.toolisticon.maven.plugin

import org.apache.maven.model.Plugin
import java.io.File

interface PluginWrapper {

  val plugin: Plugin

}

data class ResourceData(
  val directory: File,
  val targetPath: String? = null
)
