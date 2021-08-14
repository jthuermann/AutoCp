package gather.models

import database.models.Problem
import settings.langSettings.model.Lang

sealed class GenerateFileErr(val problem: Problem, message: String?) : Exception(message) {
    class LangNotConfiguredErr(problem: Problem) : GenerateFileErr(problem, null)
    class FileTemplateMissingErr(val lang: Lang, problem: Problem) : GenerateFileErr(problem, null)
    class FileAlreadyExistsErr(val filePath: String, problem: Problem) : GenerateFileErr(problem, null)
}