package database

import database.models.ProblemData
import database.models.ProblemSpec
import database.models.ProblemState
import database.models.TestcaseSpec
import org.jetbrains.exposed.sql.Database

interface AutoCp {

    @Deprecated("this is not necessary")
    val instance: Database

    // ProblemData
    fun addProblemData(data: ProblemData)
    fun getProblemData(solutionPath: String): ProblemData?

    fun associateSolutionWithProblem(solutionPath: String, problemSpec: ProblemSpec)

    fun addTestcaseSpec(spec: TestcaseSpec)

    fun updateTestcaseSpecs(specs: List<TestcaseSpec>)
    fun updateProblemState(state: ProblemState)

}
