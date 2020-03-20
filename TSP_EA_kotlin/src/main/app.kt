package main

import DATASETS_DIR
import main.problem.ConsoleLogger
import main.problem.evolutionary.*
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

    val filepath = "$DATASETS_DIR\\${filenames[3]}"

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

