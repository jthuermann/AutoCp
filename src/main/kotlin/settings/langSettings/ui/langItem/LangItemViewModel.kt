package settings.langSettings.ui.langItem

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.lang.Language
import common.isIndex
import common.isNotIndex
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import settings.langSettings.model.BuildConfig
import settings.langSettings.model.Lang
import settings.langSettings.ui.dialogs.buildConfigDialog.BuildConfigDialog
import ui.helpers.biState
import ui.vvm.ViewModel

class LangItemViewModel(
    parentScope: CoroutineScope?,
    val lang: MutableStateFlow<Lang?>,
) : ViewModel(parentScope) {

    val buildConfigs = lang
        .biState(scope, listOf(), { it?.buildConfigs ?: listOf() }, { this?.copy(buildConfigs = it) })

    val selectedConfigIndex = MutableStateFlow(-1)

    val defaultBuildConfigIndex = lang.biState(scope, -1, {
        it?.let {
            it.buildConfigs.indexOfFirst { config -> config.id == it.defaultBuildConfigId }
        } ?: -1
    }, {
        this?.let { lang ->
            if (lang.buildConfigs.isIndex(it))
                lang.copy(defaultBuildConfigId = lang.buildConfigs[it].id)
            else
                null
        }
    })

    val fileTemplates = lang.map {
        it?.let {
            val fileType = Language.findLanguageByID(it.langId)?.associatedFileType!!
            val manager = FileTemplateManager.getDefaultInstance()
            listOf(*manager.allJ2eeTemplates, *manager.allTemplates, *manager.internalTemplates)
                .filter { template -> template.isTemplateOfType(fileType) }

        } ?: listOf()
    }.stateIn(scope, SharingStarted.Lazily, listOf())

    val selectedFileTemplateIndex = lang.biState(scope, -1, {
        it?.fileTemplateName?.let { name -> fileTemplates.value.indexOfFirst { t -> t.name == name } } ?: -1
    }, {
        this?.copy(fileTemplateName = fileTemplates.value.getOrNull(it)?.name)
    })


    fun addNewConfig() {
        val list = buildConfigs.value.toMutableList()
        val index = selectedConfigIndex.value
        val newBlankConfig = BuildConfig(System.currentTimeMillis(), "", "")
        val config = BuildConfigDialog(newBlankConfig, list).showAndGetConfig() ?: return

        list.add(index + 1, config)
        scope.launch {
            buildConfigs.emit(list)
            selectedConfigIndex.emit(index + 1)
        }
    }


    fun editConfig() {

        val list = buildConfigs.value.toMutableList()
        val index = selectedConfigIndex.value
        if (list.isNotIndex(index))
            return
        val config = BuildConfigDialog(list[index], list).showAndGetConfig() ?: return

        list[index] = config
        scope.launch {
            buildConfigs.emit(list)
        }

    }
}