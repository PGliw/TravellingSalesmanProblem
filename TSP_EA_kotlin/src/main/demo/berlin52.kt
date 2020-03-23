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
            "Population size",
            gaProblemBerlin52.initializer::populationSize,
            { newPopulationSize: Int ->
                gaProblemBerlin52.logger =
                    FileLogger(
                        "$RESULTS_DIR\\${berlin52}\\ex_1_1_population_size_${berlin52}_log_population_size_${newPopulationSize}.csv",
                        true
                    )
                gaProblemBerlin52.initializer.populationSize = newPopulationSize
            },
            generateSequence(10) { it + 5 }.takeWhile { it < 51 }.toSet(),
            10,
            "$RESULTS_DIR\\${berlin52}\\ex_1_1_population_size_berlin_52.csv"
        ),
        // 1.2. Number of generations
        Experiment(
            gaProblemBerlin52,
            "Generations number",
            gaProblemBerlin52::generations,
            { newGenerations: Int ->
                gaProblemBerlin52.logger =
                    FileLogger(
                        "$RESULTS_DIR\\${berlin52}\\ex_1_2_generations_no_${berlin52}_log_generations_no_${newGenerations}.csv",
                        true
                    )
                gaProblemBerlin52.generations = newGenerations
            },
            generateSequence(5) { it + 5 }.takeWhile { it < 101 }.toSet(),
            10,
            "$RESULTS_DIR\\${berlin52}\\ex_1_2_generations_no_${berlin52}.csv"
        ),
        // 1.3. Tournament size - this id the ugly one, it uses type casting
        Experiment(
            gaProblemBerlin52population20,
            "Tournament size",
            gaProblemBerlin52population20::selector,
            { newSelector: ISelector ->
                val tournamentSize = when (newSelector) {
                    is TournamentSelector -> newSelector.participants.toString()
                    else -> throw InvalidSelctorException("Expected tournament selector")
                }
                gaProblemBerlin52.logger =
                    FileLogger(
                        "$RESULTS_DIR\\${berlin52}\\ex_1_3_tournament_size_${berlin52}_log_tournament_size_${tournamentSize}.csv",
                        true
                    )
                gaProblemBerlin52population20.selector = newSelector
            },
            setOf(2, 4, 6, 8, 10, 12, 14, 16, 18, 20).map { TournamentSelector(it, random) }.toSet(),
            10,
            "$RESULTS_DIR\\${berlin52}\\ex_1_3_tournament_size_${berlin52}.csv"
        ) {
            when (it) {
                is TournamentSelector -> it.participants.toString()
                else -> throw InvalidSelctorException("Expected tournament selector")
            }
        },
        // 2. Selection type
        Experiment(
            gaProblemBerlin52,
            "Selection type",
            gaProblemBerlin52::selector,
            { newSelector: ISelector ->
                gaProblemBerlin52.logger =
                    FileLogger(
                        "$RESULTS_DIR\\${berlin52}\\ex_2_selection_type_${berlin52}_log_selection_type_${newSelector.selectorType()}.csv",
                        true
                    )
                gaProblemBerlin52.selector = newSelector
            },
            setOf(TournamentSelector(10, random), RouletteSelector(0.2, random)),
            10,
            "$RESULTS_DIR\\${berlin52}\\ex_2_selection_type_${berlin52}.csv"
        ) { it.selectorType() },
        // 3.1. CrossOver probability
        Experiment(
            gaProblemBerlin52,
            "Cross over probability",
            gaProblemBerlin52::crossOverProbability,
            { newCrossOverProbability ->
                gaProblemBerlin52.logger =
                    FileLogger(
                        "$RESULTS_DIR\\${berlin52}\\ex_3_1_crossover_prob_${berlin52}_log_crossover_prob_${newCrossOverProbability}.csv",
                        true
                    )
                gaProblemBerlin52.crossOverProbability = newCrossOverProbability
            },
            generateSequence(0.05) { it + 0.05 }.takeWhile { it < 1.0 }.map { it.toFloat() }.toSet(),
            10,
            "$RESULTS_DIR\\${berlin52}\\ex_3_1_crossover_prob_${berlin52}_2.csv"
        ),
        // 3.2 Mutation probability
        Experiment(
            gaProblemBerlin52,
            "Mutation probability",
            gaProblemBerlin52::mutationProbability,
            { newMutationProbability ->
                gaProblemBerlin52.logger =
                    FileLogger(
                        "$RESULTS_DIR\\${berlin52}\\ex_3_2_mutation_prob_${berlin52}_log_mutation_prob_${newMutationProbability}.csv",
                        true
                    )
                gaProblemBerlin52.mutationProbability = newMutationProbability
            },
            generateSequence(0.05) { it + 0.05 }.takeWhile { it < 1.0 }.map { it.toFloat() }.toSet(),
            10,
            "$RESULTS_DIR\\${berlin52}\\ex_3_2_mutation_prob_${berlin52}_2.csv"
        )
        // TODO: 4. GA vs naive methods - random and greedy
    ).forEach { it.conduct() }
}