package main.problem

import main.utils.RouteNotFoundException
import kotlin.random.Random


typealias Route = List<City>

/**
 * Represents an instance of Travelling Salesman Problem
 * @param filePath path to the file which is used to load cities' data
 * @param logger ILogger that logs changes while solving the problem. Null if no logging is performed.
 * @param random Random instance used in random algorithms while solving the problem.
 */
abstract class Problem(
    filePath: String, var logger: ILogger? = null, val random: Random = Random
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

    /**
     * Returns route as the solution of TSP
     * Logs the result by logger
     */
    fun solve(): Route {
        val result = solution()
        logger?.logLine("${fitness(result)}")
        logger?.close()
        return result
    }

    /**
     * Repeats solving the TSP `repetitions` times and returns aggregate result
     * @param repetitions number of repetitions that solving the problem will be repeated
     * @return (best result, worst result, average result, standard deviation) of all repetitions
     */
    fun examine(repetitions: Int): ExaminationResult {
        if (repetitions <= 0) throw IllegalArgumentException("Number of repetitions must be greater than 0 (now: $repetitions)")
        val results = mutableListOf<Float>()
        repeat(repetitions) {
            val result = solution()
            results.add(fitness(result))
        }
        logger?.close()
        return ExaminationResult(
            results.min() ?: throw RouteNotFoundException("Best route not found"),
            results.max() ?: throw RouteNotFoundException("Worst route not found"),
            results.average().toFloat(),
            results.std().toFloat()
        )
    }

    protected abstract fun solution(): Route
}








