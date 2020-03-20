package main.problem

import kotlin.random.Random

typealias Route = List<City>

abstract class Problem(filePath: String, protected val random: Random = Random) :
    IFitnessProvider {
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

    abstract fun solve(): Route
}


interface IFitnessProvider {
    fun fitness(route: Route): Float
}

interface IFitnessReceiver {
    fun register(provider: IFitnessProvider)
}








