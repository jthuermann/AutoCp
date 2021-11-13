package com.github.pushpavel.autocp.settings.projectSettings

import com.github.pushpavel.autocp.settings.projectSettings.cmake.cmakeProjectSection
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.project.Project
import com.intellij.ui.layout.panel

class AutoCpProjectSettingsConfigurable(val project: Project) : BoundConfigurable("Project") {
    private val projectSettings = project.autoCpProject()
    var extension = projectSettings.defaultFileExtension
    var askEveryTime = projectSettings.askBeforeFileGeneration

    override fun createPanel() = panel {
        titledRow("Solution File Generation") {
            row("File Extension") {
                textField(::extension, 2)
                    .onReset { extension = projectSettings.defaultFileExtension }
                    .onIsModified { extension != projectSettings.defaultFileExtension }
                    .onApply {
                        if (extension.isNotBlank())
                            projectSettings.defaultFileExtension = extension.trim().replace(".", "")
                    }.withValidationOnInput {
                        if (it.text.isNotBlank())
                            null
                        else
                            error("Should not be empty")
                    }
            }
            row {
                checkBox("Ask every time before generating files", ::askEveryTime)
                    .onReset { askEveryTime = projectSettings.askBeforeFileGeneration }
                    .onIsModified { askEveryTime != projectSettings.askBeforeFileGeneration }
                    .onApply { projectSettings.askBeforeFileGeneration = askEveryTime }
            }
            cmakeProjectSection(project)
        }
    }

}
