package plugin.settings

import com.intellij.openapi.components.*
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "plugin.settings.AppSettingsState",
    storages = [Storage("autoCpPluginSettings.xml")]
)
@Service
class AutoCpSettings : PersistentStateComponent<AutoCpSettings> {
    var preferredLanguage: String? = "cpp"
    var solutionLanguages: MutableList<SolutionLanguage> = getDefaultSolutionLanguages()
    var selectedIndex: Int? = 0

    override fun getState() = this

    override fun loadState(state: AutoCpSettings) = XmlSerializerUtil.copyBean(state, this)

    companion object {
        val instance = service<AutoCpSettings>()

        val DUPLICATE_NAME_REGEX = Regex("^(.*)_([0-9]+)\$")
        val INPUT_PATH_KEY = "@input@"
        val OUTPUT_PATH_KEY = "@output@"

        fun getSolutionLanguageTemplate(): SolutionLanguage {
            return SolutionLanguage(
                "C++",
                "cpp",
                "g++ $INPUT_PATH_KEY -o $OUTPUT_PATH_KEY"
            )
        }

        fun getDefaultSolutionLanguages(): MutableList<SolutionLanguage> {
            return mutableListOf(
                SolutionLanguage("C++", "cpp", "g++ $INPUT_PATH_KEY -o $OUTPUT_PATH_KEY "),
                SolutionLanguage("java", "java", "javac $INPUT_PATH_KEY --output $OUTPUT_PATH_KEY ")
            )
        }
    }
}