package settings.langSettings.model

import com.intellij.icons.AllIcons
import kotlinx.serialization.Serializable
import settings.generalSettings.AutoCpGeneralSettings
import common.ui.swing.TileCellRenderer

@Serializable
data class BuildConfig(
    val id: Long,
    val name: String,
    val buildCommand: String
) {
    companion object {
        fun cellRenderer(emptyText: String = "None"): TileCellRenderer<BuildConfig> {
            return TileCellRenderer(emptyText = emptyText) {
                text = it.name
                icon = AllIcons.RunConfigurations.Applet
            }
        }
    }

    fun constructBuildCommand(inputPath: String, outputPath: String): String {
        return buildCommand
            .replace(AutoCpGeneralSettings.INPUT_PATH_KEY, inputPath)
            .replace(AutoCpGeneralSettings.OUTPUT_PATH_KEY, outputPath)
    }

    constructor(m: MutableBuildConfig) : this(m.id, m.name, m.buildCommand)
}


data class MutableBuildConfig(
    var id: Long = -1,
    var name: String = "",
    var buildCommand: String = "",
) {
    constructor(c: BuildConfig) : this(c.id, c.name, c.buildCommand)
}