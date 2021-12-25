package com.github.pushpavel.autocp.config

import com.github.pushpavel.autocp.core.persistance.storable
import com.github.pushpavel.autocp.core.persistance.storables.solutions.Solutions
import com.intellij.execution.Executor
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.LocatableConfigurationBase
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializer
import org.jdom.Element
import kotlin.io.path.Path
import kotlin.io.path.nameWithoutExtension

/**
 * Implementation Class for a Custom Run Configuration that can also be created from context (by right-clicking and run)
 */
class AutoCpConfig(project: Project, factory: ConfigurationFactory, name: String) :
    LocatableConfigurationBase<RunProfileState>(project, factory, name) {

    var solutionFilePath: String = ""

    private val solutionFiles = project.storable<Solutions>()

    /**
     * Returns [RunProfileState] that defines the execution of this Run Configuration
     */
    override fun getState(executor: Executor, environment: ExecutionEnvironment) = AutoCpRunState(project, this)


    /**
     * Returns UI Editor for this Run Configuration
     */
    override fun getConfigurationEditor() = ConfigEditor(project, this)


    /**
     * Suggests Name for Run configurations created from Context (by right-clicking and run)
     */
    override fun suggestedName(): String {
        return solutionFiles[solutionFilePath]?.displayName ?: Path(solutionFilePath).nameWithoutExtension
    }

    override fun writeExternal(element: Element) {
        XmlSerializer.serializeInto(this, element)
        super.writeExternal(element)
    }

    override fun readExternal(element: Element) {
        super.readExternal(element)
        XmlSerializer.deserializeInto(this, element)
    }

}