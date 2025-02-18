package main.problem.research

import main.problem.ExaminationResult
import main.problem.Problem
import main.problem.evolutionary.GAProblem
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter

fun List<ExaminationResult>.saveToFile(
    filepath: String,
    prefixHeader: String,
    prefixes: List<String>
) {
    PrintWriter(BufferedWriter(FileWriter(filepath, true))).let { out ->
        out.println("$prefixHeader;best;worst;avg;std")
        prefixes.zip(this).forEach {
            out.println("${it.first};${it.second.best};${it.second.worst};${it.second.avg};${it.second.std}")
        }
        out.close()
    }
}

fun <T> Problem.investigateSingleParam(
    restoreValue: T,
    paramValues: Set<T>,
    repetitions: Int,
    stateChange: ((T) -> Unit)
): List<ExaminationResult> {
    val results = mutableListOf<ExaminationResult>()
    val paramsToExamine = paramValues.toMutableList() // list of parameters to be checked
    while (paramsToExamine.isNotEmpty()) {
        stateChange(paramsToExamine.removeFirst()) // check next parameter
        results.add(examine(repetitions))
    }
    stateChange(restoreValue) // revert initial parameter value
    return results.toList()
}