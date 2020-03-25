package main.demo

import RESULTS_DIR
import main.problem.FileLogger
import main.problem.evolutionary.*
import main.problem.research.Experiment
import main.utils.InvalidSelctorException

data class ExaminationSet(val dataSetName: String,
                          val exPopulationSizeGaProblem: GAProblem, val populationSizes: Set<Int>,
                          val exGenerationsNoGaProblem: GAProblem, val generationsNumbers: Set<Int>,
                          val exTournamentSizeGaProblem: GAProblem, val tournamentSizes: Set<Int>,
                          val exSelectionTypeGaProblem: GAProblem, val selectionTypes: Set<ISelector>,
                          val exCrossOverProbabilityGaProblem: GAProblem, val crossOverProbabilities: Set<Float>,
                          val exMutationProbabilityGaProblem: GAProblem, val mutationProbabilities: Set<Float>
)

/**
 * Performs examination (analysis) of GA parameters on berlin52.tsp file
 * Saves results in /results/berlin52 dir
 */
fun ExaminationSet.performExams(){

    listOf(
        // 1.1. Population size
        Experiment(
            exPopulationSizeGaProblem,
            "Data set;Population size",
            exPopulationSizeGaProblem.initializer::populationSize,
            { newPopulationSize: Int ->
                exPopulationSizeGaProblem.logger =
                    FileLogger(
                        "$RESULTS_DIR\\ex_1_1_population_size_log.txt",
                        true,
                        "$dataSetName;$newPopulationSize"
                    )
                exPopulationSizeGaProblem.initializer.populationSize = newPopulationSize
            },
            populationSizes,
            10,
            "$RESULTS_DIR\\ex_1_1_population_size.txt",
            {newPopulationSize: Int -> "$dataSetName;$newPopulationSize" }
        ),

        // 1.2. Number of generations
        Experiment(
            exGenerationsNoGaProblem,
            "Data set;Generations number",
            exGenerationsNoGaProblem::generations,
            { newGenerations: Int ->
                exGenerationsNoGaProblem.logger =
                    FileLogger(
                        "$RESULTS_DIR\\ex_1_2_generations_no_log.txt",
                        true,
                        "$dataSetName;$newGenerations"
                    )
                exGenerationsNoGaProblem.generations = newGenerations
            },
            generationsNumbers,
            10,
            "$RESULTS_DIR\\ex_1_2_generations_no.txt",
            {newGenerations: Int -> "$dataSetName;$newGenerations" }
        ),

        // 1.3. Tournament size - this id the ugly one, it uses type casting
        Experiment(
            exTournamentSizeGaProblem,
            "Data set;Tournament size",
            exTournamentSizeGaProblem::selector,
            { newSelector: ISelector ->
                val tournamentSize = when (newSelector) {
                    is TournamentSelector -> newSelector.participants.toString()
                    else -> throw InvalidSelctorException("Expected tournament selector")
                }
                exTournamentSizeGaProblem.logger =
                    FileLogger(
                        "$RESULTS_DIR\\ex_1_3_tournament_size_log.txt",
                        true,
                        "$dataSetName;$tournamentSize"
                    )
                exTournamentSizeGaProblem.selector = newSelector
            },
            tournamentSizes.map { TournamentSelector(it, exTournamentSizeGaProblem.random) }.toSet(),
            10,
            "$RESULTS_DIR\\ex_1_3_tournament_size.txt"
        ) {
            when (it) {
                is TournamentSelector -> "$$dataSetName;${it.participants}"
                else -> throw InvalidSelctorException("Expected tournament selector")
            }
        },

        // 2. Selection type
        Experiment(
            exSelectionTypeGaProblem,
            "Data set;Selection type",
            exSelectionTypeGaProblem::selector,
            { newSelector: ISelector ->
                exSelectionTypeGaProblem.logger =
                    FileLogger(
                        "$RESULTS_DIR\\ex_2_selection_type_log.txt",
                        true,
                        "$dataSetName;${newSelector.selectorType()}"
                    )
                exSelectionTypeGaProblem.selector = newSelector
            },
            selectionTypes,
            10,
            "$RESULTS_DIR\\ex_2_selection_type.txt"
        ) { "$dataSetName;${it.selectorType()}" },

        // 3.1. CrossOver probability
        Experiment(
            exCrossOverProbabilityGaProblem,
            "Data set;Cross over probability",
            exCrossOverProbabilityGaProblem::crossOverProbability,
            { newCrossOverProbability ->
                exCrossOverProbabilityGaProblem.logger =
                    FileLogger(
                        "$RESULTS_DIR\\ex_3_1_crossover_prob_log.txt",
                        true,
                        "$dataSetName;$newCrossOverProbability"
                    )
                exCrossOverProbabilityGaProblem.crossOverProbability = newCrossOverProbability
            },
            crossOverProbabilities,
            10,
            "$RESULTS_DIR\\ex_3_1_crossover_prob.txt"
        ){ "$dataSetName;$it"},

        // 3.2 Mutation probability
        Experiment(
            exMutationProbabilityGaProblem,
            "Data set;Mutation probability",
            exMutationProbabilityGaProblem::mutationProbability,
            { newMutationProbability ->
                exMutationProbabilityGaProblem.logger =
                    FileLogger(
                        "$RESULTS_DIR\\ex_3_2_mutation_prob_log.txt",
                        true,
                        "$dataSetName;$newMutationProbability"
                    )
                exMutationProbabilityGaProblem.mutationProbability = newMutationProbability
            },
            mutationProbabilities,
            10,
            "$RESULTS_DIR\\ex_3_2_mutation_prob.txt"
        ){"$dataSetName;$it"}
        // TODO: 4. GA vs naive methods - random and greedy
    ).forEach { it.conduct() }
}