package main.problem.evolutionary

import main.problem.City
import main.problem.Route
import kotlin.random.Random

/**
 * Initializes a population for generic algorithm
 * @property populationSize size of population initialized by initializer
 */
interface IInitializer {
    var populationSize: Int
    fun initialize(cities: List<City>): Set<Route>
}

/**
 * Initializes a population randomly for generic algorithm
 * @property populationSize size of population initialized by initializer
 * @property random Random instance used for random generation of initial population
 */
class RandomInitializer(override var populationSize: Int, private val random: Random) :
    IInitializer {
    override fun initialize(cities: List<City>): Set<Route> = main.algorithms.initialize(cities, populationSize, random)
}