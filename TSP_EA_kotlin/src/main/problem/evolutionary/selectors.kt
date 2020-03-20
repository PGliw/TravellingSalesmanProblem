package main.problem.evolutionary

import main.utils.FitnessProviderNotRegisteredException
import main.algorithms.rouletteSelection
import main.algorithms.tournamentSelection
import main.problem.IFitnessProvider
import main.problem.IFitnessReceiver
import main.problem.Route
import kotlin.random.Random

interface ISelector : IFitnessReceiver {
    fun select(population: List<Route>): Route
}

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
}

class TournamentSelector(
    private val participants: Int,
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
}