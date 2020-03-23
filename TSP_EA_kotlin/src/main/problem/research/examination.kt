package main.problem.research

import main.problem.Problem
import main.problem.evolutionary.GAProblem
import main.utils.RouteNotFoundException
import kotlin.math.pow
import kotlin.math.sqrt

data class ExaminationResult(val best: Float, val worst: Float, val avg: Float, val std: Float)

class Experiment<T>(
    private val gaProblem: GAProblem,
    private val parameterName: String,
    getRestorePoint: (() -> T),
    private val stateChange: ((T) -> Unit),
    private val paramValues: Set<T>,
    private val repetitions: Int,
    private val filepath: String,
    private val paramPrint: ((T) -> String) = {it.toString()}
    ) {
    private val restorePoint = getRestorePoint()
    fun conduct() {
        val results = gaProblem.investigateSingleParam(restorePoint, paramValues, repetitions, stateChange)
        results.saveToFile(filepath, parameterName, paramValues.map { paramPrint(it) })
    }
}


private fun <T> GAProblem.investigateSingleParam(
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

fun Problem.examine(repetitions: Int): ExaminationResult {
    if (repetitions <= 0) throw IllegalArgumentException("Number of repetitions must be greater than 0 (now: $repetitions)")
    val results = mutableListOf<Float>()
    repeat(repetitions) {
        val result = solve()
        results.add(fitness(result))
    }
    return ExaminationResult(
        results.min() ?: throw RouteNotFoundException("Best route not found"),
        results.max() ?: throw RouteNotFoundException("Worst route not found"),
        results.average().toFloat(),
        results.std().toFloat()
    )
}

fun Collection<Float>.std(): Double {
    val avg = average()
    val sd = fold(0.0) { accumulator, next ->
        accumulator + (next - avg).pow(2)
    }
    return sqrt(sd / size)
}