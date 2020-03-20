package mian.algorithms

import mian.*
import kotlin.random.Random

/**
 * @param cities list of all cities to be visited
 * @param distanceFun function calculating distance between cities
 * @param fitnessFun cost function to be optimized
 * @return route optimized by greedy algorithm
 */
fun greedyAlgorithm(
    cities: List<City>,
    distanceFun: (City, City) -> Float = { city1, city2 -> city1.distTo(city2) },
    fitnessFun: (List<City>) -> Float = { route -> fitness(route) }
): Route {
    val routes = mutableSetOf<List<City>>()
    for (firstCity in cities) {
        val citiesWaiting = mutableSetOf<City>()
        citiesWaiting.addAll(cities)
        citiesWaiting.remove(firstCity)
        val route = mutableSetOf(firstCity)
        while (citiesWaiting.isNotEmpty()) {
            val closestCity = citiesWaiting.minBy { city -> distanceFun(city, route.last()) }
                ?: throw RouteNotFoundException("Closest city not found")
            citiesWaiting.remove(closestCity)
            route.add(closestCity)
        }
        routes.add(route.toList())
    }
    return routes.minBy { route -> fitnessFun(route) } ?: throw RouteNotFoundException("No route found")
}


/**
 * @param cities list of all cities to be visited
 * @param draws number of draws (potential results)
 * @param fitnessFun cost function to be optimized
 * @param random Random generator used for route generation
 * @return route with lowest mian.fitness function value within the draws
 */
fun randomAlgorithm(
    cities: List<City>,
    draws: Int,
    random: Random = Random,
    fitnessFun: (List<City>) -> Float = { route -> fitness(route) }
): Route {
    var i = 0
    var bestResult = Float.MAX_VALUE
    var result: List<City>? = null
    while (i < draws) {
        val candidate = randomRoute(cities, random)
        val fitness = fitnessFun(candidate)
        if (fitness < bestResult) {
            bestResult = fitness
            result = candidate
        }
        i++
    }
    return result ?: throw RouteNotFoundException("rendomAlghorim did not find result")
}
