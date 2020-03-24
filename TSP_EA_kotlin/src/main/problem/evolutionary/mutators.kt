package main.problem.evolutionary

import main.algorithms.orderChangingMutation
import main.problem.Route
import kotlin.random.Random

/**
 * Performs mutation operation in generic algorithm
 */
interface IMutator {
    fun mutate(route: Route): Route
}

/**
 * Swaps two randomly chosen cities in a route
 * @param random Random instance used for random selection of cities to swap
 */
class SwapMutator(private val random: Random) : IMutator {
    override fun mutate(route: Route) = orderChangingMutation(route, random)
}
