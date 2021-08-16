package tester

import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessOutputTypes
import com.intellij.execution.testframework.sm.ServiceMessageBuilder
import com.intellij.execution.testframework.sm.ServiceMessageBuilder.*
import common.errors.Err
import common.errors.NoReachErr
import common.errors.presentableString
import common.res.R
import settings.langSettings.model.BuildConfig
import tester.base.BuildErr
import tester.base.ProcessRunner
import tester.tree.ResultNode
import tester.tree.TestNode
import tester.tree.TreeTestingProcess

/**
 * Class that abstracts formatting and sending output to the console and the TestRunner UI
 */
class TreeTestingProcessReporter(private val processHandler: ProcessHandler) : TreeTestingProcess.Listener {
    override fun leafStart(node: TestNode.Leaf) {
        testStarted(node.name).apply()
        testStdOut(node.name).addAttribute(
            "out",
            "${"___".repeat(5)}[ ${node.name} ]${"___".repeat(5)}\n"
        ).apply()
    }

    override fun leafFinish(node: ResultNode.Leaf) {
        val nodeName = node.sourceNode.name
        if (node.verdict is tester.errors.Verdict.CorrectAnswer) {
            testStdOut(nodeName).addAttribute("out", node.verdict.output.let {
                if (it.endsWith('\n')) it
                else it + '\n'
            }).apply()

            testFinished(nodeName)
                .addAttribute("duration", node.verdict.executionTime.toString())
                .apply()

            return
        }

        when (node.verdict) {
            is tester.errors.Verdict.InternalErr -> TODO()
            is tester.errors.Verdict.RuntimeErr -> TODO()
            is tester.errors.Verdict.TimeLimitErr -> TODO()
            is tester.errors.Verdict.WrongAnswer -> TODO()
            else -> throw NoReachErr
        }
    }

    override fun groupStart(node: TestNode.Group) {
        testSuiteStarted(node.name).apply()
    }

    override fun groupFinish(node: ResultNode.Group) {
        testSuiteFinished(node.sourceNode.name).apply()
    }

    override fun compileStart(configName: String, buildConfig: BuildConfig) {
        processHandler.notifyTextAvailable(
            R.strings.startCompilingMsg(configName, buildConfig) + "\n",
            ProcessOutputTypes.STDOUT
        )
    }

    override fun compileFinish(result: Result<ProcessRunner.CapturedResults>) {
        when {
            result.isSuccess -> {
                val r = result.getOrThrow()
                val msg = R.strings.compileSuccessMsg(r.output, r.executionTime)
                processHandler.notifyTextAvailable(msg + "\n", ProcessOutputTypes.STDOUT)
            }
            result.isFailure -> {
                val msg = when (val e = result.exceptionOrNull()!!) {
                    is BuildErr -> R.strings.buildErrMsg(e)
                    else -> throw e
                }
                processHandler.notifyTextAvailable(msg + "\n", ProcessOutputTypes.STDERR)
            }
        }
    }


    override fun testingProcessStartErrored(error: Err) {
        processHandler.notifyTextAvailable(
            error.presentableString() + "\n", ProcessOutputTypes.STDERR
        )

        processHandler.notifyTextAvailable(error.stackTraceToString(), ProcessOutputTypes.STDERR)
    }

    override fun testingProcessError(message: String) {
        processHandler.notifyTextAvailable(message + "\n", ProcessOutputTypes.STDERR)
    }

    private fun ServiceMessageBuilder.apply() {
        processHandler.notifyTextAvailable(
            this.toString() + "\n", ProcessOutputTypes.STDOUT
        )
    }
}