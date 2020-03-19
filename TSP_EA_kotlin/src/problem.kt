import kotlin.random.Random

typealias Route = List<City>

class Problem(filePath: String) {
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

    private fun fitnessFun(route: Route) = fitness(route, this::distanceFun)

    fun greedy() = greedyAlgorithm(cities, this::distanceFun, this::fitnessFun)

    fun random(draws: Int) = randomAlgorithm(cities, draws, this::fitnessFun)
}
