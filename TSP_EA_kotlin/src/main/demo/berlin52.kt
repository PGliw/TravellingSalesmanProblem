package main.demo

import DATASETS_DIR
import RESULTS_DIR
import main.problem.ConsoleLogger
import main.problem.FileLogger
import main.problem.evolutionary.*
import main.problem.research.Experiment
import main.utils.InvalidSelctorException
import kotlin.random.Random

/**
 * Performs examination (analysis) of GA parameters on berlin52.tsp file
 * Saves results in /results/berlin52 dir
 */
fun examBerlin52(){
    // File "berlin52.tsp"
    val berlin52 = "berlin52"
    val filepathBerlin52 = "$DATASETS_DIR\\$berlin52.tsp"
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
            "Data set;Population size",
            gaProblemBerlin52.initializer::populationSize,
            { newPopulationSize: Int ->
                gaProblemBerlin52.logger =
                    FileLogger(
                        "$RESULTS_DIR\\ex_1_1_population_size_log.txt",
                        true,
                        "$berlin52;$newPopulationSize"
                    )
                gaProblemBerlin52.initializer.populationSize = newPopulationSize
            },
            generateSequence(10) { it + 5 }.takeWhile { it < 51 }.toSet(),
            10,
            "$RESULTS_DIR\\ex_1_1_population_size.txt",
            {newPopulationSize: Int -> "$berlin52;$newPopulationSize" }
        ),
        // 1.2. Number of generations
        Experiment(
            gaProblemBerlin52,
            "Data set;Generations number",
            gaProblemBerlin52::generations,
            { newGenerations: Int ->
                gaProblemBerlin52.logger =
                    FileLogger(
                        "$RESULTS_DIR\\ex_1_2_generations_no_log.txt",
                        true,
                        "$berlin52;$newGenerations"
                    )
                gaProblemBerlin52.generations = newGenerations
            },
            generateSequence(5) { it + 5 }.takeWhile { it < 101 }.toSet(),
            10,
            "$RESULTS_DIR\\ex_1_2_generations_no.txt",
            {newGenerations: Int -> "$berlin52;$newGenerations" }
        ),
        // 1.3. Tournament size - this id the ugly one, it uses type casting
        Experiment(
            gaProblemBerlin52population20,
            "Data set;Tournament size",
            gaProblemBerlin52population20::selector,
            { newSelector: ISelector ->
                val tournamentSize = when (newSelector) {
                    is TournamentSelector -> newSelector.participants.toString()
                    else -> throw InvalidSelctorException("Expected tournament selector")
                }
                gaProblemBerlin52.logger =
                    FileLogger(
                        "$RESULTS_DIR\\ex_1_3_tournament_size_log.txt",
                        true,
                        "$berlin52;$tournamentSize"
                    )
                gaProblemBerlin52population20.selector = newSelector
            },
            setOf(2, 4, 6, 8, 10, 12, 14, 16, 18, 20).map { TournamentSelector(it, random) }.toSet(),
            10,
            "$RESULTS_DIR\\ex_1_3_tournament_size.txt"
        ) {
            when (it) {
                is TournamentSelector -> "$$berlin52;${it.participants}"
                else -> throw InvalidSelctorException("Expected tournament selector")
            }
        },
        // 2. Selection type
        Experiment(
            gaProblemBerlin52,
            "Data set;Selection type",
            gaProblemBerlin52::selector,
            { newSelector: ISelector ->
                gaProblemBerlin52.logger =
                    FileLogger(
                        "$RESULTS_DIR\\ex_2_selection_type_log.txt",
                        true,
                        "$berlin52;${newSelector.selectorType()}"
                    )
                gaProblemBerlin52.selector = newSelector
            },
            setOf(TournamentSelector(10, random), RouletteSelector(0.2, random)),
            10,
            "$RESULTS_DIR\\ex_2_selection_type.txt"
        ) { "$berlin52;${it.selectorType()}" },
        // 3.1. CrossOver probability
        Experiment(
            gaProblemBerlin52,
            "Data set;Cross over probability",
            gaProblemBerlin52::crossOverProbability,
            { newCrossOverProbability ->
                gaProblemBerlin52.logger =
                    FileLogger(
                        "$RESULTS_DIR\\ex_3_1_crossover_prob_log.txt",
                        true,
                        "$berlin52;$newCrossOverProbability"
                    )
                gaProblemBerlin52.crossOverProbability = newCrossOverProbability
            },
            generateSequence(0.05) { it + 0.05 }.takeWhile { it < 1.0 }.map { it.toFloat() }.toSet(),
            10,
            "$RESULTS_DIR\\ex_3_1_crossover_prob.txt"
        ){ "$berlin52;$it"},
        // 3.2 Mutation probability
        Experiment(
            gaProblemBerlin52,
            "Data set;Mutation probability",
            gaProblemBerlin52::mutationProbability,
            { newMutationProbability ->
                gaProblemBerlin52.logger =
                    FileLogger(
                        "$RESULTS_DIR\\ex_3_2_mutation_prob_log.txt",
                        true,
                        "$berlin52;$newMutationProbability"
                    )
                gaProblemBerlin52.mutationProbability = newMutationProbability
            },
            generateSequence(0.05) { it + 0.05 }.takeWhile { it < 1.0 }.map { it.toFloat() }.toSet(),
            10,
            "$RESULTS_DIR\\ex_3_2_mutation_prob.txt"
        ){"$berlin52;$it"}
        // TODO: 4. GA vs naive methods - random and greedy
    ).forEach { it.conduct() }
}