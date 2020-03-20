package main.algorithms

import main.problem.City
import kotlin.random.Random

/**
 * @param cities list of cities to be shuffled
 * @return random sequence of cities
 */
fun randomRoute(cities: List<City>, random: Random = Random) = cities.shuffled(random)