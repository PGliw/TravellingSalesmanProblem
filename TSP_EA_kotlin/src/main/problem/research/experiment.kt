package main.problem.research

import main.problem.evolutionary.GAProblem


class Experiment<T>(
    private val gaProblem: GAProblem,
    private val parameterName: String,
    getRestorePoint: (() -> T),
    private val stateChange: ((T) -> Unit),
    private val paramValues: Set<T>,
    private val repetitions: Int,
    private val filepath: String,
    private val paramPrint: ((T) -> String) = { it.toString() }
) {
    private val restorePoint = getRestorePoint()
    fun conduct() {
        val results = gaProblem.investigateSingleParam(restorePoint, paramValues, repetitions, stateChange)
        results.saveToFile(filepath, parameterName, paramValues.map { paramPrint(it) })
    }
}


