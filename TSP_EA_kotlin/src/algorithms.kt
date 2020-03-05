/**
 * @param cities list of all cities
 * @return route optimized by greedy algorithm
 */
fun greedyAlgorithm(cities: List<City>): List<City> {
    val routes = mutableSetOf<List<City>>()
    for (firstCity in cities) {
        val citiesWaiting = mutableSetOf<City>()
        citiesWaiting.addAll(cities)
        citiesWaiting.remove(firstCity)
        val route = mutableSetOf(firstCity)
        while (citiesWaiting.isNotEmpty()) {
            val closestCity = citiesWaiting.minBy { city -> city.distTo(route.last()) }
                ?: throw RouteNotFoundException("Closest city not found")
            citiesWaiting.remove(closestCity)
            route.add(closestCity)
        }
        routes.add(route.toList())
    }
    return routes.minBy { route -> fitness(route) } ?: throw RouteNotFoundException("No route found")
}


/**
 * @param draws number of draws (potential result)
 * @return best of draws (with lowest fitness function value)
 */
fun randomAlgorithm(cities: List<City>, draws: Int): List<City> {
    var i = 0
    var bestResult = Float.MAX_VALUE
    var result: List<City>? = null
    while (i < draws) {
        val candidate = cities.shuffled()
        val fitness = fitness(candidate)
        if (fitness < bestResult) {
            bestResult = fitness
            result = candidate
        }
        i++
    }
    return result ?: throw RouteNotFoundException("rendomAlghorim did not found result")
}