@file:Suppress("MemberVisibilityCanBePrivate")

package com.github.pushpavel.autocp.common.res

import com.github.pushpavel.autocp.build.settings.LangNotConfiguredErr
import com.github.pushpavel.autocp.config.validators.SolutionFilePathErr
import com.github.pushpavel.autocp.database.models.Problem
import com.github.pushpavel.autocp.gather.models.GenerateFileErr
import com.github.pushpavel.autocp.settings.generalSettings.OpenFileOnGather
import com.github.pushpavel.autocp.tester.base.BuildErr
import com.github.pushpavel.autocp.tester.errors.ProcessRunnerErr
import com.github.pushpavel.autocp.tester.errors.Verdict


object AutoCpStrings {
    const val runConfigName = "AutoCp Solution"
    const val runConfigId = "AutoCp"
    const val runConfigDescription = "Test your Competitive Programming solution with AutoCp"


    // File Issue notes
    const val fileIssue = "please file an issue (https://github.com/Pushpavel/AutoCp/issues/new/choose)"

    fun defaultFileIssue(e: Exception) = "" +
            "An error had occurred. If it is not expected, $fileIssue\n\n" +
            "Error message:\n${e.localizedMessage}\n" +
            "Stacktrace:\n${e.stackTraceToString()}"

    fun fatalFileIssue(e: Exception) = "" +
            "This was not supposed to happen, $fileIssue\n\n" +
            "Error message:\n${e.localizedMessage}\n" +
            "Stacktrace:\n${e.stackTraceToString()}"

    // problemGatheringDialog strings
    const val problemGatheringDialogMsg =
        "You can always change these settings at Settings/Preferences > Tools > AutoCp > Project"

    // Settings strings
    const val projectSettingsOverrideMsg = "Some of these settings could be overridden at Tools > AutoCp > Project"


    // General Settings strings
    const val openFilesOnGatherText = "Open files after generating"

    fun OpenFileOnGather.presentable() = when (this) {
        OpenFileOnGather.NONE -> "None"
        OpenFileOnGather.ONLY_FIRST -> "Only first"
        OpenFileOnGather.ALL -> "All"
    }

    const val fileGenerationRootComment = "Relative to project root<br><br>" +
            "Macros:<br>" +
            "<b>${R.keys.groupNameMacro}</b> : name of the contest or category name"


    // Project Settings CMake
    const val addToCMakeMsg = "Add generated solution files to CMakeLists.txt"

    // Build Configuration Dialog strings
    const val commandTemplateDesc = "<html>" +
            "The executable in these commands are run in an isolated temporary directory. But not the command itself.<br>" +
            "So, relative path to executable won't work and should use <b>\$dir</b> to build absolute path.<br>" +
            "Make sure you wrap this absolute path with double quotes.<br><br>" +
            "Macros:<br>" +
            "<b>${R.keys.inputPathMacro}</b> : absolute path to a solution file with quotes.<br>" +
            "<b>${R.keys.dirUnquotedPathMacro}</b> : absolute path to the isolated temp directory without quotes.<br>" +
            "<b>${R.keys.dirPathMacro}</b> : \"<b>\$dir</b>\"" +
            "</html>"

    const val buildCommandComment = "Run once before testing begins, usually should compile or generate an executable"
    const val executeCommandComment = "" +
            "Run for each testcase. usually should run the executable generated by Build Command. execution time of this command is considered for TLE verdict (Time Limit Exceeded)"

    // Common Err strings
    const val noReachErrMsg = "Execution should not have reached this line, $fileIssue"


    // Problem Gathering Action strings
    const val startGatheringText = "Start problem gathering service"
    const val stopGatheringText = "Stop problem gathering service"

    const val startGatheringDesc =
        "Start an AutoCp service that listens for problem data coming from competitive companion browser extension"

    const val stopGatheringDesc =
        "Stops an AutoCp service that is listening for problem data coming from competitive companion browser extension"

    const val gatheringServiceOnStart = "Start problem gathering service when project loads"
    const val gatheringServiceOnStartDesc = "" +
            "problem gathering service listens for problem data coming " +
            "from competitive companion browser extension to " +
            "generate files."

    // Solution File Generation Messages
    fun fileGenFailedTitle(name: String) = "$name file is not created"

    const val langNotConfiguredMsg = "" +
            "No Programming Language is configured with AutoCp. " +
            "Please configure languages that you use at Settings/Preference > Tools > AutoCp > Languages"

    fun fileAlreadyExistsMsg(e: GenerateFileErr.FileAlreadyExistsErr) = "" +
            "File already exists.\nPath to file: ${e.filePath}"

    // Problem Gathering Service Server strings
    const val serverTitle = "Problem Gathering Service"
    const val serverRunningMsg = "" +
            "Started AutoCp Problem Gathering Service...\n" +
            "Open the problem/ contest page in the browser and " +
            "press the Competitive companion button to generate solution files.\n\n" +
            "You can start/stop this service at Tools > $stopGatheringText\n" +
            "To prevent starting this service on project load, go to Settings/Preferences > Tools > AutoCp"

    const val serverStoppedMsg = "Service has been stopped. " +
            "Use Tools > $startGatheringText to start it."

    fun portTakenMsg(port: Int) = "Port $port is already in use."

    fun portRetryMsg(port: Int) = "Retrying with port $port..."

    fun allPortFailedMsg() = "" +
            "Could not find a free port to use with competitive companion. " +
            "You may be running multiple instances of AutoCp installed IDEs or other tools that use competitive companion. " +
            "Try closing other programs or restarting your pc. If this issue still occurs, " + fileIssue


    // Problem Gathering Service Gathering strings
    const val problemGatheringTitle = "Problem Gathering"


    fun gatheredReport(problems: List<Problem>, total: Int?): String {
        if (total == null) return ""
        return "(${problems.size}/${total}) problems gathered.\n" +
                problems.joinToString(separator = "\n") { "\t" + it.name }
    }

    // Testing Process Strings

    fun solutionFilePathErrMsg(e: SolutionFilePathErr) = "" +
            "solutionFilePath in the run configuration \"${e.configName}\" has issues\n" +
            when (e) {
                is SolutionFilePathErr.BlankPathErr -> "It must not be empty"
                is SolutionFilePathErr.FileDoesNotExist -> "This file does not exists, ${e.pathString}"
                is SolutionFilePathErr.FileNotRegistered -> "AutoCp is not enabled for this file, ${e.pathString}"
                is SolutionFilePathErr.FormatErr -> "It is in invalid format."
            }

    fun langNotConfiguredErrMsg(e: LangNotConfiguredErr) = "" +
            "File Extension \".${e.extension}\" is not configured\n" +
            "Fix this issue at Settings/Preferences > Tools > AutoCp > Languages > +"

    // Testing Compile Strings

    fun commandReadyMsg(configName: String) = "" +
            "Ready to execute \"$configName\""

    fun startCompilingMsg(configName: String) = "" +
            "Building \"$configName\" ..."

    fun compileSuccessMsg(log: String, executionMills: Long) = "" +
            "Build completed in ${executionMills}ms\n" + log

    fun buildErrMsg(e: BuildErr) = "" +
            "Error while running the below command\n${e.command}\n\n" +
            when (e.err) {
                is ProcessRunnerErr.RuntimeErr -> e.err.localizedMessage
                is ProcessRunnerErr.TimeoutErr -> "Took longer than ${e.err.timeLimit}ms to execute"
                is ProcessRunnerErr.DeadProcessErr -> "Trying to run a process which is already dead, $fileIssue"
                else -> throw e.err
            }

    // Testing Verdict Strings
    fun verdictOneLine(verdict: Verdict) = when (verdict) {
        is Verdict.CorrectAnswer -> "[+] SUCCESS: CORRECT ANSWER"
        is Verdict.WrongAnswer -> "[-] FAILURE: WRONG ANSWER"
        is Verdict.TimeLimitErr -> "[-] FAILURE: TIME LIMIT EXCEEDED"
        is Verdict.RuntimeErr -> "[-] FAILURE: RUNTIME ERROR"
        is Verdict.InternalErr -> "[+/-] UNKNOWN: COULD NOT JUDGE"
    }
}

fun String.failed(): String = "$this Failed"
fun String.cancelled(): String = "$this Cancelled"

fun String.success(): String = "$this Successful"