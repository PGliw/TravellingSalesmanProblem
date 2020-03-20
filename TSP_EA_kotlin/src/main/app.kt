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

    val greedyProblem =
        GreedyProblem("${DATASETS_DIR}\\${filenames[3]}", ConsoleLogger())

    println("---------Greedy route---------")
    greedyProblem.solve()
    println()

    val randomProblem =
        RandomProblem(
            "${DATASETS_DIR}\\${filenames[3]}",
            10,
            ConsoleLogger()
        )

    println("---------Random problem---------")
    randomProblem.solve()
    println()

    val random = Random(64)
    val gaProblem = GAProblem(
        "${DATASETS_DIR}\\${filenames[3]}",
        RandomInitializer(20, random),
//        RouletteSelector(0.7, random),
        TournamentSelector(10),
        OrderedCrosser(random),
        SwapMutator(random),
        0.5f,
        0.6f,
        200,
        ConsoleLogger(),
        random
    )

    println("---------GA problem---------")
    gaProblem.solve()
    println()
}