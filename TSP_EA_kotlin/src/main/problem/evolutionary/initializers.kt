package main.problem.evolutionary

import main.problem.City
import main.problem.Route
import kotlin.random.Random

interface IInitializer {
    var populationSize: Int
    fun initialize(cities: List<City>): Set<Route>
}

class RandomInitializer(override var populationSize: Int, private val random: Random) :
    IInitializer {
    override fun initialize(cities: List<City>): Set<Route> = main.algorithms.initialize(cities, populationSize, random)
}