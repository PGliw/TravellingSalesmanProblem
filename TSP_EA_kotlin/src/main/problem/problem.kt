package main.problem

import kotlin.random.Random

typealias Route = List<City>

abstract class Problem(
    private val filePath: String, var logger: ILogger? = null, val random: Random = Random
) : IFitnessProvider {
    protected val cities = loadData(filePath)
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

    protected fun distanceFun(city1: City, city2: City) =
        distances[Pair(city1, city2)] ?: error("Pair of cities (${city1.id}, ${city2.id})")

    override fun fitness(route: Route) =
        fitness(route, this::distanceFun)

    fun solve(): Route {
        val result = solution()
        logger?.logLine("${fitness(result)}")
        logger?.close()
        return result
    }

    protected abstract fun solution(): Route
}


interface IFitnessProvider {
    fun fitness(route: Route): Float
}

interface IFitnessReceiver {
    fun register(provider: IFitnessProvider)
}








