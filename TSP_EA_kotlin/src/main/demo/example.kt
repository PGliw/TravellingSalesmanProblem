package main.demo

import main.problem.ConsoleLogger
import main.problem.evolutionary.*
import main.problem.traditional.GreedyProblem
import main.problem.traditional.RandomProblem
import kotlin.random.Random

/**
 * Presents example of working algorithms
 */
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