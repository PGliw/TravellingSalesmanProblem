package main.problem.traditional

import main.algorithms.greedyAlgorithm
import main.algorithms.randomAlgorithm
import main.problem.Problem
import main.problem.Route
import kotlin.random.Random

class GreedyProblem(filePath: String, random: Random = Random) : Problem(filePath, random) {
    override fun solve(): Route =
        greedyAlgorithm(cities, this::distanceFun, this::fitness)
}

class RandomProblem(filePath: String, private val draws: Int, random: Random = Random) : Problem(filePath, random) {
    override fun solve(): Route =
        randomAlgorithm(cities, draws, random, this::fitness)
}