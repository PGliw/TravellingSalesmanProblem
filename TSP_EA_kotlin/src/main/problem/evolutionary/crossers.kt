package main.problem.evolutionary

import main.algorithms.orderedCrossOver
import main.problem.Route
import kotlin.random.Random

interface ICrosser {
    fun crossOver(parent1: Route, parent2: Route): Route
}

class OrderedCrosser(private val random: Random) : ICrosser {
    override fun crossOver(parent1: Route, parent2: Route) = orderedCrossOver(parent1, parent2, random)
}
