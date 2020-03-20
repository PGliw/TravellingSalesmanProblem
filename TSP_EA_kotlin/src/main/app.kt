package main

import DATASETS_DIR
import RESULTS_DIR
import main.problem.ConsoleLogger
import main.problem.evolutionary.*
import main.problem.research.investigateCrossOverProbability
import main.problem.research.saveToFile
import main.problem.traditional.GreedyProblem
import main.problem.traditional.RandomProblem
import kotlin.random.Random

fun main() {

    val filenames = listOf(
        "ali535.tsp",
        "berlin11_modified.tsp",
        "berlin52.tsp",
        "fl417.tsp",
        "gr666.tsp",
        "kroA100.tsp",
        "kroA150.tsp",
        "kroA200.tsp",
        "nrw1379.tsp",
        "pr2392.tsp"
    )
    val filepath = "$DATASETS_DIR\\${filenames[2]}"

    val random = Random(167)
    val ga1 = GAProblem(
        filepath,
        RandomInitializer(10, random),
        TournamentSelector(10, random),
        OrderedCrosser(random),
        SwapMutator(random),
        0.5f, 0.2f, 100,
        ConsoleLogger(), random
    )

    val crossOverProbabilities = generateSequence(0.05) { it + 0.05 }.takeWhile { it < 1.0 }.map { it.toFloat() }.toSet()
    ga1.investigateCrossOverProbability(crossOverProbabilities, 10)
        .saveToFile(
            "$RESULTS_DIR\\ex_3_1_crossover_prob_${filenames[2]}_2.csv",
            "Cross over probability",
            crossOverProbabilities.map { it.toString() }
        )
}

fun example(filepath: String) {
    val random = Random(10)
    println("----- Greedy -----")
    GreedyProblem(filepath, ConsoleLogger(), random).solve()
    println()

    println("----- Random -----")
    RandomProblem(filepath, 60, ConsoleLogger(), random).solve()
    println()

    println("----- Genetic Algorithm -----")
    GAProblem(
        filepath,
        RandomInitializer(10, random),
        TournamentSelector(10, random),
        OrderedCrosser(random),
        SwapMutator(random),
        0.5f, 0.2f, 100,
        ConsoleLogger(), random
    ).solve()
    println()
}

