package mian

import mian.algorithms.*
import kotlin.random.Random

typealias Route = List<City>

abstract class Problem(filePath: String, protected val random: Random = Random) : IFitnessProvider {
    protected val cities = loadData(filePath)
    private val distances = mutableMapOf<Pair<City, City>, Float>().apply {
        for (city1 in cities) {
            for (city2 in cities) {
                this[Pair(city1, city2)] = when {
                    this.contains(Pair(city2, city1)) -> this[Pair(city2, city1)]!!
                    city1 == city2 -> 0f
                    else -> city1.distTo(city2)
                }
            }
        }
    }.toMap()

    protected fun distanceFun(city1: City, city2: City) =
        distances[Pair(city1, city2)] ?: error("Pair of cities (${city1.id}, ${city2.id})")

    override fun fitness(route: Route) =
        fitness(route, this::distanceFun)

    abstract fun solve(): Route
}

class GreedyProblem(filePath: String, random: Random = Random) : Problem(filePath, random) {
    override fun solve(): Route =
        greedyAlgorithm(cities, this::distanceFun, this::fitness)
}

class RandomProblem(filePath: String, private val draws: Int, random: Random = Random) : Problem(filePath, random) {
    override fun solve(): Route =
        randomAlgorithm(cities, draws, random, this::fitness)
}


interface IFitnessProvider {
    fun fitness(route: Route): Float
}

interface IFitnessReceiver {
    fun register(provider: IFitnessProvider)
}

interface IInitializer {
    fun initialize(cities: List<City>): Set<Route>
}

class RandomInitializer(private val populationSize: Int, private val random: Random) : IInitializer {
    override fun initialize(cities: List<City>): Set<Route> = initialize(cities, populationSize, random)
}

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

interface ICrosser {
    fun crossOver(parent1: Route, parent2: Route): Route
}

class OrderedCrosser(private val random: Random) : ICrosser {
    override fun crossOver(parent1: Route, parent2: Route) = orderedCrossOver(parent1, parent2, random)
}

interface IMutator {
    fun mutate(route: Route): Route
}

class SwapMutator(private val random: Random) : IMutator {
    override fun mutate(route: Route) = orderChangingMutation(route, random)
}

class GAProblem(
    filePath: String,
    private val initializer: IInitializer,
    private val selector: ISelector,
    private val crosser: ICrosser,
    private val mutator: IMutator,
    private val populationSize: Int,
    private val crossOverProbability: Float,
    private val mutationProbability: Float,
    private val generations: Int,
    random: Random = Random
) :
    Problem(filePath, random) {

    init {
        if (populationSize < 0) throw IllegalArgumentException("Population size must not be less than 0 ($populationSize)")
        if (crossOverProbability < 0 || crossOverProbability > 1) throw IllegalArgumentException("crossOverProbability must be between 0.0 and 1.0. Now is: $crossOverProbability")
        if (mutationProbability < 0 || mutationProbability > 1) throw IllegalArgumentException("crossOverProbability must be between 0.0 and 1.0. Now is: $mutationProbability")
        selector.register(this)
    }

    override fun solve(): Route {
        val initialPopulation = initializer.initialize(cities)
        var bestResult: Route? = null
        var population = initialPopulation
        for (generationNo in 1..generations) {
            val newGeneration = mutableSetOf<Route>()
            while (newGeneration.size < populationSize) {
                val parent1 = selector.select(population.toList())
                val parent2 = selector.select(population.toList())
                val next = when {
                    random.nextFloat() < crossOverProbability -> crosser.crossOver(parent1, parent2)
                    else -> parent1
                }.let {
                    when {
                        random.nextFloat() < mutationProbability -> mutator.mutate(it)
                        else -> it
                    }
                }
                newGeneration.add(next)
            }
            population = newGeneration
            bestResult = population.minBy { fitness(it) }
//            println("generation #$generationNo: ${bestResult?.map{it.id}}, ${fitnessFun(bestResult!!)}")
//            println("generation #$generationNo - sum: ${population.sumByDouble { fitnessFun(it).toDouble() }}")
        }
        return bestResult ?: throw RouteNotFoundException("Generic algorithm did not found the route")
    }
}
