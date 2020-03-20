package main.problem.evolutionary

import main.algorithms.orderChangingMutation
import main.problem.Route
import kotlin.random.Random

interface IMutator {
    fun mutate(route: Route): Route
}

class SwapMutator(private val random: Random) : IMutator {
    override fun mutate(route: Route) = orderChangingMutation(route, random)
}
