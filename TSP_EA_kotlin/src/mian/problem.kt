package mian

import kotlin.random.Random

typealias Route = List<City>

class Problem(filePath: String, private val random: Random = Random) {
    private val cities = loadData(filePath)
    private val distances = mutableMapOf<Pair<City, City>, Float>().apply {
        for (city1 in cities) {
            for (city2 in cities) {
                this[Pair(city1, city2)] = when {
                    this.contains(Pair(city2, city1)) -> this[Pair(city2, city1)]!!
                    city1 == city2 -> 0f
                    else -> city1.distTo(city2)
                }
            }
        }
    }.toMap()

    private fun distanceFun(city1: City, city2: City) =
        distances[Pair(city1, city2)] ?: error("Pair of cities (${city1.id}, ${city2.id})")

    private fun fitnessFun(route: Route) =
        fitness(route, this::distanceFun)

    fun greedy() = greedyAlgorithm(cities, this::distanceFun, this::fitnessFun)

    fun random(draws: Int) = randomAlgorithm(cities, draws, random, this::fitnessFun)

    fun generic(
        populationSize: Int,
        crossOverProbability: Float,
        mutationProbability: Float,
        generations: Int,
        participants: Int
    ): Route {
        if (populationSize < 0) throw IllegalArgumentException("Population size must not be less than 0 ($populationSize)")
        if (crossOverProbability < 0 || crossOverProbability > 1) throw IllegalArgumentException("crossOverProbability must be between 0.0 and 1.0. Now is: $crossOverProbability")
        if (mutationProbability < 0 || mutationProbability > 1) throw IllegalArgumentException("crossOverProbability must be between 0.0 and 1.0. Now is: $mutationProbability")
        val initialPopulation: Set<Route> =
            generateSequence { randomRoute(cities, random) }.take(populationSize).toSet() // TODO population size
        var bestResult: Route? = null
        var population = initialPopulation
        for (generationNo in 1..generations) {
            val newGeneration = mutableSetOf<Route>()
            while (newGeneration.size < populationSize) {
                val parent1 = tournamentSelection(
                    population.toList(),
                    participants,
                    random,
                    this::fitnessFun
                )
                val parent2 = tournamentSelection(
                    population.toList(),
                    participants,
                    random,
                    this::fitnessFun
                )
                val next = when {
                    random.nextFloat() < crossOverProbability -> orderedCrossOver(
                        parent1,
                        parent2
                    )
                    else -> parent1
                }.let {
                    when {
                        random.nextFloat() < mutationProbability -> orderChangingMutation(it)
                        else -> it
                    }
                }
                newGeneration.add(next)
            }
            population = newGeneration
            bestResult = population.minBy { fitnessFun(it) }
        }
        return bestResult ?: throw RouteNotFoundException("Generic algorithm did not found the route")
    }
}
