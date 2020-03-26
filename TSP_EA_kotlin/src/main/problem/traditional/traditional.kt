package main.problem.traditional

import main.algorithms.greedyAlgorithm
import main.algorithms.randomAlgorithm
import main.problem.ILogger
import main.problem.Problem
import main.problem.Route
import kotlin.random.Random

class GreedyProblem(filePath: String, logger: ILogger? = null, random: Random = Random) :
    Problem(filePath, logger, random) {
    override fun solution(): Route = greedyAlgorithm(cities, this::distanceFun, this::fitness)
}

class RandomProblem(filePath: String, var draws: Int, logger: ILogger? = null, random: Random = Random) :
    Problem(filePath, logger, random) {
    override fun solution(): Route = randomAlgorithm(cities, draws, random, this::fitness)
}