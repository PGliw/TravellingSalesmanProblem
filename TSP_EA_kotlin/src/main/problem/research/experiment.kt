package main.problem.research

import main.problem.evolutionary.GAProblem

/**
 * @param gaProblem Generic Algorithm Problem which would be the subject of this experiment
 * @param parameterHeader header of the column with parameter values - in most cases: parameter name (eg. population size)
 * @param getRestorePoint returns initial value of parameter which is changed throughout the experiment
 * @param stateChange state change that occurs foreach parameter value
 * @param paramValues set of values that parameter takes throughout the experiment
 * @param repetitions number of times the experiment is repeated for single parameter value
 * @param filepath file path where the final results of the experiment are saved
 * @param paramPrint returns parameter value formatted to string, which will be used to denote parameter value in text file
 */
class Experiment<T>(
    private val gaProblem: GAProblem,
    private val parameterHeader: String,
    getRestorePoint: (() -> T),
    private val stateChange: ((T) -> Unit),
    private val paramValues: Set<T>,
    private val repetitions: Int,
    private val filepath: String,
    private val paramPrint: ((T) -> String) = { it.toString() }
) {
    private val restorePoint = getRestorePoint()

    /**
     * Investigates influence of given parameter change on the generic algorithm.
     * Saves results to a file given by filepath
     */
    fun conduct() {
        val results = gaProblem.investigateSingleParam(restorePoint, paramValues, repetitions, stateChange)
        results.saveToFile(filepath, parameterHeader, paramValues.map { paramPrint(it) })
    }
}


