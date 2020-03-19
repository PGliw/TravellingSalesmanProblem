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
): List<City> {
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
 * @return best of draws (route with lowest fitness function value)
 */
fun randomAlgorithm(
    cities: List<City>,
    draws: Int,
    fitnessFun: (List<City>) -> Float = { route -> fitness(route) }
): List<City> {
    var i = 0
    var bestResult = Float.MAX_VALUE
    var result: List<City>? = null
    while (i < draws) {
        val candidate = randomRoute(cities)
        val fitness = fitnessFun(candidate)
        if (fitness < bestResult) {
            bestResult = fitness
            result = candidate
        }
        i++
    }
    return result ?: throw RouteNotFoundException("rendomAlghorim did not found result")
}
