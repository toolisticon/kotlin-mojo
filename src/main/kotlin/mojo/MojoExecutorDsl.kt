package io.toolisticon.maven.mojo

import io.toolisticon.maven.model.*
import org.apache.maven.artifact.ArtifactUtils
import org.apache.maven.model.Plugin
import org.twdata.maven.mojoexecutor.MojoExecutor
import java.io.File

object MojoExecutorDsl {

  fun Plugin.gavKey(): GavKey = ArtifactUtils.key(groupId, artifactId, version)
  fun goal(goal: Goal): Goal = MojoExecutor.goal(goal)
  fun plugin(groupId: GroupId, artifactId: ArtifactId, version: ArtifactVersion) = MojoExecutor.plugin(groupId, artifactId, version)

  fun configuration(receiver: ElementListDsl.() -> Unit): Configuration = MojoExecutor.configuration(*receiverToArray(receiver))

  class ElementListDsl(internal val list: MutableList<MojoExecutor.Element> = mutableListOf()) {

    fun element(elementName: ElementName, value: String) = list.add(MojoExecutor.element(elementName, value))
    fun element(elementName: ElementName, value: File) = element(elementName, value.path)
    fun element(elementName: ElementName, value: Any) = element(elementName, "$value")

    fun element(elementName: ElementName, receiver: ElementListDsl.() -> Unit) {
      list.add(MojoExecutor.element(elementName, *receiverToArray(receiver)))
    }
  }

  fun receiverToArray(receiver: ElementListDsl.() -> Unit): Array<MojoExecutor.Element> {
    val dsl = ElementListDsl()
    receiver.invoke(dsl)
    return dsl.list.toTypedArray()
  }
}
