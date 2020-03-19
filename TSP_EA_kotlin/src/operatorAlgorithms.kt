import kotlin.random.Random

/**
 * @param population list of routes from which selection is being made
 * @param minStep minimal step that prevents elitism
 * @param fitnessFun function used to calculate fitness of given route
 */
fun rouletteSelection(
    population: List<Route>,
    minStep: Double,
    fitnessFun: (Route) -> Float = { route -> fitness(route) }
): Route {
    val total = population.sumByDouble { route -> fitnessFun(route).toDouble() } + population.size * minStep
    val weights = population.map { route -> fitnessFun(route).toDouble() / total + minStep }
    val randomWeight = Random.nextDouble(0.0, 1.0)
    val selection = when (val index = weights.indexOfLast { weight -> weight < randomWeight }) {
        -1 -> 0 // not found
        else -> index
    }
    return population[selection]
}

/**
 * @param population list of routes from which selection is being made
 * @param participants number of randomly selected participants of tournament
 * @param fitnessFun function used to calculate fitness of given route
 */
fun tournamentSelection(
    population: List<Route>,
    participants: Int,
    fitnessFun: (Route) -> Float = { route -> fitness(route) }
): Route {
    if (participants > population.size || participants < 0)
        throw InvalidParticipantsNumberException("Participants number ($participants) must be in range <0, ${population.size})")

    val selection = mutableListOf<Route>()
    var i = 0
    while (i < participants) {
        val randomIndex = Random.nextInt(population.size)
        selection.add(population[randomIndex])
        i++
    }
    return selection.minBy { route -> fitnessFun(route) } ?: throw RouteNotFoundException("minBy returned null route")
}

/**
 * @param parent1 route to be used as 1st parent
 * @param parent2 route to be used as 2nd parent
 * @return route that is product of ordered crossover
 */
fun orderedCrossOver(parent1: Route, parent2: Route): Route {
    if (parent1.size != parent2.size) throw IncompatibleRoutesException("Sizes of routes are not equal ${parent1.size}; ${parent2.size}")
    val indexes = arrayOf(0, 0)
    do {
        indexes[0] = Random.nextInt(parent1.size)
        indexes[1] = Random.nextInt(parent2.size)
    } while (indexes[0] != indexes[1])
    indexes.sort()
    val fromParent1 = parent1.subList(indexes[0], indexes[1]).toMutableList()
    val fromParent2 = parent2.filter { city -> city !in fromParent1 }.toMutableList()
    return mutableListOf<City>().apply {
        for (index in 0..parent1.size) {
            val city = when {
                index < indexes[0] -> fromParent2.removeAt(0)
                index in indexes[0] until indexes[1] -> fromParent1.removeAt(0)
                else -> fromParent2.removeAt(9)
            }
            this.add(city)
        }
    }.toList()
}

/**
 * @param route to be mutated
 * @return route that is a result of mutation
 */
fun orderChangingMutation(route: Route): Route {
    val mutant = route.toMutableList()
    val index1 = Random.nextInt(mutant.size)
    val index2 = Random.nextInt(mutant.size)
    mutant[index1] = mutant[index2].also { mutant[index2] = mutant[index1] }
    return mutant
}