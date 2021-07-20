package config

import com.google.common.io.Files
import com.intellij.execution.Executor
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.LocatableConfigurationBase
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.JDOMExternalizerUtil
import org.jdom.Element

/**
 * Implementation Class for a Custom Run Configuration that can also be created from context (by right-clicking and run)
 */
class AutoCpConfig(project: Project, factory: ConfigurationFactory, name: String) :
    LocatableConfigurationBase<RunProfileState>(project, factory, name) {

    var solutionFilePath: String = ""
    var solutionLangId: Long = -1
    var langId: String = ""
    var buildConfigId: String = ""


    /**
     * Returns [RunProfileState] that defines the execution of this Run Configuration
     */
    override fun getState(executor: Executor, environment: ExecutionEnvironment) = AutoCpRunState(this)


    /**
     * Returns UI Editor for this Run Configuration
     */
    override fun getConfigurationEditor() = ConfigEditor(project)


    /**
     * Suggests Name for Run configurations created from Context (by right-clicking and run)
     */
    override fun suggestedName(): String? {
        if (solutionFilePath.isEmpty())
            return null
        return Files.getNameWithoutExtension(solutionFilePath)
    }


    /** SERIALIZATION */
    companion object {
        // SERIALIZATION KEYS
        private const val SOLUTION_FILE_PATH = "solutionFilePath"
        private const val SOLUTION_LANG_ID = "solutionLangId"
        private const val LANG_ID = "langId"
        private const val BUILD_CONFIG_ID = "buildConfigId"
    }

    override fun writeExternal(element: Element) {
        JDOMExternalizerUtil.writeField(element, SOLUTION_FILE_PATH, solutionFilePath)
        JDOMExternalizerUtil.writeField(element, SOLUTION_LANG_ID, solutionLangId.toString())
        JDOMExternalizerUtil.writeField(element, LANG_ID, langId)
        JDOMExternalizerUtil.writeField(element, BUILD_CONFIG_ID, buildConfigId)
        super.writeExternal(element)
    }

    override fun readExternal(element: Element) {
        super.readExternal(element)
        solutionFilePath = JDOMExternalizerUtil.readField(element, SOLUTION_FILE_PATH, "")
        solutionLangId = JDOMExternalizerUtil.readField(element, SOLUTION_LANG_ID, "-1").toLong()
        langId = JDOMExternalizerUtil.readField(element, LANG_ID, "")
        buildConfigId = JDOMExternalizerUtil.readField(element, BUILD_CONFIG_ID, "")
    }

}