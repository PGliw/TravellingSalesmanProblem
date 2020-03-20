package main.problem.research

import main.problem.Problem
import main.utils.RouteNotFoundException
import kotlin.math.pow
import kotlin.math.sqrt

data class ExaminationResult(val best: Float, val worst: Float, val avg: Float, val std: Float)

fun Problem.examinate(repetitions: Int): ExaminationResult {
    if (repetitions <= 0) throw IllegalArgumentException("Number of repetitions must be greater than 0 (now: $repetitions")
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