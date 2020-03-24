package main.problem.evolutionary

import main.algorithms.rouletteSelection
import main.algorithms.tournamentSelection
import main.problem.IFitnessProvider
import main.problem.IFitnessReceiver
import main.problem.Route
import main.utils.FitnessProviderNotRegisteredException
import kotlin.random.Random

/**
 * Performs selection operation in Generic Algorithm
 */
interface ISelector : IFitnessReceiver {
    fun select(population: List<Route>): Route
    fun selectorType() = "ISelector"
}

/**
 * Performs roulette selection operation in Generic Algorithm
 * @param minStep value added to wage of each participant to enable choice of weaker routes. Must be in [0.0, 1.0]
 * @param random Random used by roulette algorithm to generate random number within (0.0, 1.0)
 */
class RouletteSelector(
    private val minStep: Double,
    private val random: Random = Random
) : ISelector {
    private var fitnessProvider: IFitnessProvider? = null

    override fun register(provider: IFitnessProvider) {
        fitnessProvider = provider
    }

    override fun select(population: List<Route>): Route = when (val fp = fitnessProvider) {
        null -> throw FitnessProviderNotRegisteredException("Fitness provider is null")
        else -> rouletteSelection(population, minStep, random, fp::fitness)
    }

    override fun selectorType() = "Roulette"
}

/**
 * Performs tournament selection operation in Generic Algorithm
 * @param participants number of particia
 * @param random Random used by roulette algorithm to generate random number within (0.0, 1.0)
 */
class TournamentSelector(
    val participants: Int,
    private val random: Random = Random
) : ISelector {
    private var fitnessProvider: IFitnessProvider? = null

    override fun register(provider: IFitnessProvider) {
        fitnessProvider = provider
    }

    override fun select(population: List<Route>): Route = when (val fp = fitnessProvider) {
        null -> throw FitnessProviderNotRegisteredException("Fitness provider is null")
        else -> tournamentSelection(population, participants, random, fp::fitness)
    }

    override fun selectorType() = "Tournament"
}