package main.demo

import DATASETS_DIR
import RESULTS_DIR
import main.problem.ConsoleLogger
import main.problem.FileLogger
import main.problem.evolutionary.*
import main.problem.research.Experiment
import main.problem.traditional.GreedyProblem
import main.problem.traditional.RandomProblem
import kotlin.random.Random

/**
 * TO BE USED ONLY FOR REPORT GENERATION in app.kt
 */
fun testGaVsNaive(){
    // Naive algorithms testing
    mapOf(
        "berlin52" to generateSequence(5) { it + 5 }.takeWhile { it < 51 }.toList(),
        "kroA200" to generateSequence(10) { it + 10 }.takeWhile { it < 200 }.toList(),
        "fl417" to generateSequence(10) { it + 10 }.takeWhile { it < 417 }.toList()
    ).forEach { pair ->
        val dataSetName = pair.key
        val filePath = "$DATASETS_DIR\\$dataSetName.tsp"
        val initialDraws = pair.value[0]
        val drawsValues = pair.value.toSet()
        val randomProblem = RandomProblem(filePath, initialDraws)
        Experiment(
            randomProblem,
            "Data Set;Number of draws",
            randomProblem::draws,
            { newDraws -> randomProblem.draws = newDraws },
            drawsValues,
            10,
            "$RESULTS_DIR\\ex_4_1_naive_random"
        ) { newDraws -> "$dataSetName;$newDraws" }
            .conduct()
    }

    val gaFileName = "ex_4_3_GA_v02"
    val random = Random(10)
    val ga1 = GAProblem("$DATASETS_DIR\\berlin52.tsp",
        RandomInitializer(25, random),
        TournamentSelector(20),
        OrderedCrosser(random),
        SwapMutator(random),
        0.5f, 0.5f, 1500,
        FileLogger("$RESULTS_DIR\\$gaFileName", true, "berlin52"), random)
    val ga2 = GAProblem("$DATASETS_DIR\\kroA200.tsp",
        RandomInitializer(100, random),
        TournamentSelector(50),
        OrderedCrosser(random),
        SwapMutator(random),
        0.5f, 0.5f, 1500,
        FileLogger("$RESULTS_DIR\\$gaFileName", true, "kroA200"), random)
    val ga3 = GAProblem("$DATASETS_DIR\\fl417.tsp",
        RandomInitializer(100, random),
        TournamentSelector(50),
        OrderedCrosser(random),
        SwapMutator(random),
        0.5f, 0.5f, 1500,
        FileLogger("$RESULTS_DIR\\$gaFileName", true, "fl417"), random)
    listOf(ga1, ga2, ga3).forEach { it.solve() }


    // Naive algorithms testing


    // Naive algorithms testing
    listOf("berlin52", "kroA200", "fl417")
        .forEach { dataSetName ->
            val filePath = "$DATASETS_DIR\\$dataSetName.tsp"
            val greedyProblem = GreedyProblem(
                filePath,
                FileLogger("$RESULTS_DIR\\ex_4_2_naive_greedy", rowPrefix = dataSetName)
            )
            greedyProblem.solve()
        }
}

/**
 * TO BE USED ONLY FOR REPORT GENERATION in app.kt
 */
fun testGaParams() {
    // File "berlin52.tsp"
    "berlin52".let { dataSetName ->
        val filePath = "$DATASETS_DIR\\$dataSetName.tsp"
        val random = Random(167)
        val gaProblemBerlin52default = GAProblem(
            filePath,
            RandomInitializer(10, random),
            TournamentSelector(10, random),
            OrderedCrosser(random),
            SwapMutator(random),
            0.5f, 0.2f, 100,
            ConsoleLogger(true), random
        )
        val gaProblemBerlin52tournament = GAProblem(
            filePath,
            RandomInitializer(22, random),
            TournamentSelector(10, random),
            OrderedCrosser(random),
            SwapMutator(random),
            0.5f, 0.2f, 100,
            ConsoleLogger(true), random
        )
        ExaminationSet(
            dataSetName,
            gaProblemBerlin52default,
            generateSequence(10) { it + 5 }.takeWhile { it < 51 }.toSet(),
            gaProblemBerlin52default,
            generateSequence(5) { it + 5 }.takeWhile { it < 101 }.toSet(),
            gaProblemBerlin52tournament,
            setOf(2, 4, 6, 8, 10, 12, 14, 16, 18, 20),
            gaProblemBerlin52default,
            setOf(
                TournamentSelector(10, gaProblemBerlin52default.random),
                RouletteSelector(0.2, gaProblemBerlin52default.random)
            ),
            gaProblemBerlin52default,
            generateSequence(0.05) { it + 0.05 }.takeWhile { it < 1.0 }.map { it.toFloat() }.toSet(),
            gaProblemBerlin52default,
            generateSequence(0.05) { it + 0.05 }.takeWhile { it < 1.0 }.map { it.toFloat() }.toSet()
        ).performExams()
    }

    // file kroA200
    "kroA200".let { dataSetName ->
        val filePath = "$DATASETS_DIR\\$dataSetName.tsp"
        val random = Random(167)
        val gaProblemKroA200default = GAProblem(
            filePath,
            RandomInitializer(100, random),
            TournamentSelector(30, random),
            OrderedCrosser(random),
            SwapMutator(random),
            0.5f, 0.2f, 100,
            ConsoleLogger(true), random
        )
        val gaProblemAli535tournament = GAProblem(
            filePath,
            RandomInitializer(200, random),
            TournamentSelector(30, random),
            OrderedCrosser(random),
            SwapMutator(random),
            0.5f, 0.2f, 100,
            ConsoleLogger(true), random
        )
        ExaminationSet(
            dataSetName,
            gaProblemKroA200default,
            generateSequence(30) { it + 10 }.takeWhile { it < 200 }.toSet(),
            gaProblemKroA200default,
            generateSequence(5) { it + 5 }.takeWhile { it < 101 }.toSet(),
            gaProblemAli535tournament,
            generateSequence(5) { it + 5 }.takeWhile { it < 200 }.toSet(),
            gaProblemKroA200default,
            setOf(
                TournamentSelector(30, gaProblemKroA200default.random),
                RouletteSelector(0.2, gaProblemKroA200default.random)
            ),
            gaProblemKroA200default,
            generateSequence(0.05) { it + 0.05 }.takeWhile { it < 1.0 }.map { it.toFloat() }.toSet(),
            gaProblemKroA200default,
            generateSequence(0.05) { it + 0.05 }.takeWhile { it < 1.0 }.map { it.toFloat() }.toSet()
        ).performExams()
    }

    // file ali535
    "fl417".let { dataSetName ->
        val filePath = "$DATASETS_DIR\\$dataSetName.tsp"
        val random = Random(167)
        val gaProblemKroA200default = GAProblem(
            filePath,
            RandomInitializer(50, random),
            TournamentSelector(20, random),
            OrderedCrosser(random),
            SwapMutator(random),
            0.5f, 0.2f, 100,
            ConsoleLogger(true), random
        )
        val gaProblemAli535tournament = GAProblem(
            filePath,
            RandomInitializer(100, random),
            TournamentSelector(20, random),
            OrderedCrosser(random),
            SwapMutator(random),
            0.5f, 0.2f, 100,
            ConsoleLogger(true), random
        )
        return ExaminationSet(
            dataSetName,
            gaProblemKroA200default,
            generateSequence(20) { it + 10 }.takeWhile { it < 417 }.toSet(),
            gaProblemKroA200default,
            generateSequence(5) { it + 5 }.takeWhile { it < 101 }.toSet(),
            gaProblemAli535tournament,
            generateSequence(5) { it + 5 }.takeWhile { it < 100 }.toSet(),
            gaProblemKroA200default,
            setOf(
                TournamentSelector(20, gaProblemKroA200default.random),
                RouletteSelector(0.2, gaProblemKroA200default.random)
            ),
            gaProblemKroA200default,
            generateSequence(0.05) { it + 0.05 }.takeWhile { it < 1.0 }.map { it.toFloat() }.toSet(),
            gaProblemKroA200default,
            generateSequence(0.05) { it + 0.05 }.takeWhile { it < 1.0 }.map { it.toFloat() }.toSet()
        ).performExams()
    }
}