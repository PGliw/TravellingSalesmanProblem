package mian

import kotlin.random.Random

/**
 * @param cities list of cities to be visited
 * @param populationSize number of routes to be returned within the set
 * @param random Random generator for generating random
 * @return randomly generated set of routes
 */
fun initialize(
    cities: List<City>,
    populationSize: Int,
    random: Random = Random
): Set<Route> = mutableSetOf<Route>().apply {
    while (size < populationSize) add(randomRoute(cities, random))
}.toSet()

/**
 * @param population list of routes from which selection is being made
 * @param minStep minimal step that prevents elitism
 * @param random Random generator used for turning roulette wheel
 * @param fitnessFun function used to calculate mian.fitness of given route
 */
fun rouletteSelection(
    population: List<Route>,
    minStep: Double,
    random: Random = Random,
    fitnessFun: (Route) -> Float = { route -> fitness(route) }
): Route {
    if (minStep < 0 || minStep > 1) throw IllegalArgumentException("minStep must be in rage 0..1 (now is $minStep)")
    val total = population.sumByDouble { route -> fitnessFun(route).toDouble() + minStep }
    val weights = population.map { route -> (fitnessFun(route).toDouble() + minStep) / total }
    val roulette = population.zip(weights)
    val randomStop = random.nextDouble(0.0, 1.0)
    var partialSum = 0.0
    for (field in roulette) {
        partialSum += field.second
        if (partialSum >= randomStop) return field.first
    }
    throw RouteNotFoundException("Roulette selection did not select any route")
}

/**
 * @param population list of routes from which selection is being made
 * @param participants number of randomly selected participants of tournament
 * @param random Random generator used for generating random index
 * @param fitnessFun function used to calculate mian.fitness of given route
 */
fun tournamentSelection(
    population: List<Route>,
    participants: Int,
    random: Random = Random,
    fitnessFun: (Route) -> Float = { route -> fitness(route) }
): Route {
    if (participants > population.size || participants < 0)
        throw IllegalArgumentException("Participants number ($participants) must be in range <0, ${population.size})")

    val selection = mutableSetOf<Route>()
    var i = 0
    while (selection.size < participants) {
        val randomIndex = random.nextInt(population.size)
        selection.add(population[randomIndex])
        i++
    }
    return selection.minBy { route -> fitnessFun(route) }
        ?: throw RouteNotFoundException("Tournament selection did not select any route")
}

/**
 * @param parent1 route to be used as 1st parent
 * @param parent2 route to be used as 2nd parent
 * @param random Random generator used for generating indexes between which the crossing over happens
 * @return route that is product of ordered crossover
 */
fun orderedCrossOver(
    parent1: Route,
    parent2: Route,
    random: Random = Random
): Route {
    if (parent1.size != parent2.size) throw IncompatibleRoutesException("Sizes of routes are not equal ${parent1.size}; ${parent2.size}")
    val indexes = arrayOf(0, 0)
    do {
        indexes[0] = random.nextInt(parent1.size)
        indexes[1] = random.nextInt(parent2.size)
    } while (indexes[0] == indexes[1])
    indexes.sort()
    val fromParent1 = parent1.subList(indexes[0], indexes[1]).toMutableList()
    val fromParent2 = parent2.filter { city -> city !in fromParent1 }.toMutableList()
    return mutableListOf<City>().apply {
        for (index in parent1.indices) {
            val city = when {
                index < indexes[0] -> fromParent2.removeAt(0)
                index in indexes[0] until indexes[1] -> fromParent1.removeAt(0)
                else -> fromParent2.removeAt(0)
            }
            this.add(city)
        }
        println("Child: ${this.map { it.id }}")
    }.toList()
}

/**
 * @param route to be mutated
 * @param random Random generator used for choosing indexes to swap
 * @return route that is a result of mutation
 */
fun orderChangingMutation(route: Route, random: Random = Random): Route {
    val mutant = route.toMutableList()
    val index1 = random.nextInt(mutant.size)
    val index2 = random.nextInt(mutant.size)
    mutant[index1] = mutant[index2].also { mutant[index2] = mutant[index1] }
    return mutant.toList()
}