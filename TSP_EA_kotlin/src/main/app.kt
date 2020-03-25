package main

import DATASETS_DIR
import main.demo.ExaminationSet
import main.demo.performExams
import main.problem.ConsoleLogger
import main.problem.evolutionary.*
import kotlin.random.Random

fun main() {

    // GA parameters testing

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
            gaProblemBerlin52default,  generateSequence(10) { it + 5 }.takeWhile { it < 51 }.toSet(),
            gaProblemBerlin52default, generateSequence(5) { it + 5 }.takeWhile { it < 101 }.toSet(),
            gaProblemBerlin52tournament, setOf(2, 4, 6, 8, 10, 12, 14, 16, 18, 20),
            gaProblemBerlin52default, setOf(TournamentSelector(10, gaProblemBerlin52default.random), RouletteSelector(0.2, gaProblemBerlin52default.random)),
            gaProblemBerlin52default, generateSequence(0.05) { it + 0.05 }.takeWhile { it < 1.0 }.map { it.toFloat() }.toSet(),
            gaProblemBerlin52default,  generateSequence(0.05) { it + 0.05 }.takeWhile { it < 1.0 }.map { it.toFloat() }.toSet()
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
            gaProblemKroA200default,  generateSequence(30) { it + 10 }.takeWhile { it < 200 }.toSet(),
            gaProblemKroA200default, generateSequence(5) { it + 5 }.takeWhile { it < 101 }.toSet(),
            gaProblemAli535tournament, generateSequence(5) { it + 5 }.takeWhile { it < 200 }.toSet(),
            gaProblemKroA200default, setOf(TournamentSelector(30, gaProblemKroA200default.random), RouletteSelector(0.2, gaProblemKroA200default.random)),
            gaProblemKroA200default, generateSequence(0.05) { it + 0.05 }.takeWhile { it < 1.0 }.map { it.toFloat() }.toSet(),
            gaProblemKroA200default,  generateSequence(0.05) { it + 0.05 }.takeWhile { it < 1.0 }.map { it.toFloat() }.toSet()
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
            gaProblemKroA200default,  generateSequence(20) { it + 10 }.takeWhile { it < 417 }.toSet(),
            gaProblemKroA200default, generateSequence(5) { it + 5 }.takeWhile { it < 101 }.toSet(),
            gaProblemAli535tournament, generateSequence(5) { it + 5 }.takeWhile { it < 100 }.toSet(),
            gaProblemKroA200default, setOf(TournamentSelector(20, gaProblemKroA200default.random), RouletteSelector(0.2, gaProblemKroA200default.random)),
            gaProblemKroA200default, generateSequence(0.05) { it + 0.05 }.takeWhile { it < 1.0 }.map { it.toFloat() }.toSet(),
            gaProblemKroA200default,  generateSequence(0.05) { it + 0.05 }.takeWhile { it < 1.0 }.map { it.toFloat() }.toSet()
        ).performExams()
    }

}



