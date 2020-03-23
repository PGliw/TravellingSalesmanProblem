package main

import DATASETS_DIR
import RESULTS_DIR
import main.problem.ConsoleLogger
import main.problem.evolutionary.*
import main.problem.research.Experiment
import main.problem.traditional.GreedyProblem
import main.problem.traditional.RandomProblem
import main.utils.InvalidSelctorException
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

    // File "berlin52.tsp"
    val berlin52 = filenames[2]
    val filepathBerlin52 = "$DATASETS_DIR\\${berlin52}"
    val random = Random(167)
    val gaProblemBerlin52 = GAProblem(
        filepathBerlin52,
        RandomInitializer(10, random),
        TournamentSelector(10, random),
        OrderedCrosser(random),
        SwapMutator(random),
        0.5f, 0.2f, 100,
        ConsoleLogger(true), random
    )
    val gaProblemBerlin52population20 = GAProblem(
        filepathBerlin52,
        RandomInitializer(22, random),
        TournamentSelector(10, random),
        OrderedCrosser(random),
        SwapMutator(random),
        0.5f, 0.2f, 100,
        ConsoleLogger(true), random
    )

    listOf(
        // 1.1. Population size
        Experiment(
            gaProblemBerlin52,
            "Population size",
            gaProblemBerlin52.initializer::populationSize,
            { newPopulationSize: Int -> gaProblemBerlin52.initializer.populationSize = newPopulationSize },
            generateSequence(10) { it + 5 }.takeWhile { it < 51 }.toSet(),
            10,
            "$RESULTS_DIR\\ex_1_1_population_size_${berlin52}.csv"
        ),
        // 1.2. Number of generations
        Experiment(
            gaProblemBerlin52,
            "Generations number",
            gaProblemBerlin52::generations,
            { newGenerations: Int -> gaProblemBerlin52.generations = newGenerations },
            generateSequence(5) { it + 5 }.takeWhile { it < 101 }.toSet(),
            10,
            "$RESULTS_DIR\\ex_1_2_generations_no_${berlin52}.csv"
        ),
        // 1.3. Tournament size TODO: fix
        Experiment(
            gaProblemBerlin52population20,
            "Tournament size",
            gaProblemBerlin52population20::selector,
            { newSelector: ISelector -> gaProblemBerlin52population20.selector = newSelector },
            setOf(2, 4, 6, 8, 10, 12, 14, 16, 18, 20).map { TournamentSelector(it, random) }.toSet(),
            10,
            "$RESULTS_DIR\\ex_1_3_tournament_size_${berlin52}.csv"
        ) {
            when (it) {
                is TournamentSelector -> it.participants.toString()
                else -> throw InvalidSelctorException("Expected tournament selector")
            }
        },
        // 2. Selection type TODO: 2 params? Or logging details?
        Experiment(
            gaProblemBerlin52,
            "Selection type",
            gaProblemBerlin52::selector,
            { newSelector: ISelector -> gaProblemBerlin52.selector = newSelector },
            setOf(TournamentSelector(10, random), RouletteSelector(0.2, random)),
            10,
            "$RESULTS_DIR\\ex_2_selection_type_${berlin52}.csv"
        ) { it.selectorType() },
        // 3.1. CrossOver probability
        Experiment(
            gaProblemBerlin52,
            "Cross over probability",
            gaProblemBerlin52::crossOverProbability,
            { newCrossOverProbability -> gaProblemBerlin52.crossOverProbability = newCrossOverProbability },
            generateSequence(0.05) { it + 0.05 }.takeWhile { it < 1.0 }.map { it.toFloat() }.toSet(),
            10,
            "$RESULTS_DIR\\ex_3_1_crossover_prob_${berlin52}_2.csv"
        ),
        // 3.2 Mutation probability
        Experiment(
            gaProblemBerlin52,
            "Mutation probability",
            gaProblemBerlin52::mutationProbability,
            { newMutationProbability -> gaProblemBerlin52.mutationProbability = newMutationProbability },
            generateSequence(0.05) { it + 0.05 }.takeWhile { it < 1.0 }.map { it.toFloat() }.toSet(),
            10,
            "$RESULTS_DIR\\ex_3_2_mutation_prob_${berlin52}_2.csv"
        )
        // TODO: 4. GA vs naive methods - random and greedy
    ).forEach { it.conduct() }

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

