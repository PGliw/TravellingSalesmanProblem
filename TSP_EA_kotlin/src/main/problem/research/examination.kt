package main.problem.research

import main.problem.Problem
import main.problem.evolutionary.GAProblem
import main.problem.evolutionary.ISelector
import main.problem.evolutionary.TournamentSelector
import main.utils.RouteNotFoundException
import kotlin.math.pow
import kotlin.math.sqrt

data class ExaminationResult(val best: Float, val worst: Float, val avg: Float, val std: Float)

private fun <T> GAProblem.investigateSingleParam(
    paramValues: Set<T>,
    repetitions: Int,
    stateChange: ((T) -> Unit)
): List<ExaminationResult> {
    val results = mutableListOf<ExaminationResult>()
    val paramsToExamine = paramValues.toMutableList() //
    while (paramsToExamine.isNotEmpty()) {
        stateChange(paramsToExamine.removeFirst()) //
        results.add(examine(repetitions))
    }
    return results.toList()
}

fun GAProblem.investigateMutationProbability(mutationProbabilities: Set<Float>, repetitions: Int) =
    investigateSingleParam(mutationProbabilities, repetitions) { newProbability ->
        mutationProbability = newProbability
    }

fun GAProblem.investigateCrossOverProbability(crossOverProbabilities: Set<Float>, repetitions: Int) =
    investigateSingleParam(crossOverProbabilities, repetitions) { newProbability ->
        mutationProbability = newProbability
    }

fun GAProblem.investigateSelectionOperators(selectionOperators: Set<ISelector>, repetitions: Int) =
    investigateSingleParam(selectionOperators, repetitions) { newSelector -> selector = newSelector }


fun GAProblem.investigateTournamentSize(tournamentSizes: Set<Int>, repetitions: Int) =
    investigateSingleParam(tournamentSizes, repetitions) { newTournamentSize ->
        selector = TournamentSelector(newTournamentSize, random)
    }

fun GAProblem.investigateGenerationsNumber(generationsNumbers: Set<Int>, repetitions: Int) = investigateSingleParam(
    generationsNumbers, repetitions
) { newGenerationsNumber -> generations = newGenerationsNumber }


fun GAProblem.investigatePopulationSize(populationSizes: Set<Int>, repetitions: Int) = investigateSingleParam(
    populationSizes, repetitions
) { newPopulationSize -> initializer.populationSize = newPopulationSize }

fun Problem.examine(repetitions: Int): ExaminationResult {
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