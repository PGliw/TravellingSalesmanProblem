package main.problem.evolutionary

import main.problem.ILogger
import main.problem.Problem
import main.problem.Route
import main.utils.RouteNotFoundException
import kotlin.random.Random

class GAProblem(
    filePath: String,
    var initializer: IInitializer,
    selector: ISelector,
    private val crosser: ICrosser,
    var mutator: IMutator,
    var crossOverProbability: Float,
    var mutationProbability: Float,
    var generations: Int,
    logger: ILogger? = null,
    random: Random = Random
) :
    Problem(filePath, logger, random) {

    init {
        if (crossOverProbability < 0 || crossOverProbability > 1) throw IllegalArgumentException("crossOverProbability must be between 0.0 and 1.0. Now is: $crossOverProbability")
        if (mutationProbability < 0 || mutationProbability > 1) throw IllegalArgumentException("crossOverProbability must be between 0.0 and 1.0. Now is: $mutationProbability")
        selector.register(this)
    }

    var selector = selector
        set(value) {
            field = value
            field.register(this)
        }

    private val detailLogger: ILogger?
        get() = when {
            logger?.isDetailed() == true -> logger
            else -> null
        }

    override fun solution(): Route {
        val initialPopulation = initializer.initialize(cities)
        detailLogger?.logLine("DETAILS LOG")
        detailLogger?.logLine("Generation; Best result; Worst result; Mean result;")
        var population = initialPopulation
        var bestResult = population.minBy { fitness(it) }
            ?: throw RouteNotFoundException("Generic algorithm did not found the best route")
        var worstResult = population.maxBy { fitness(it) }
            ?: throw RouteNotFoundException("Generic algorithm did not found the worst route")
        var meanFitness = population.sumByDouble { fitness(it).toDouble() } / population.size
        detailLogger?.logLine("0; ${fitness(bestResult)}; ${fitness(worstResult)}; $meanFitness")
        for (generationNo in 1..generations) {
            val newGeneration = mutableSetOf<Route>()
            while (newGeneration.size < initializer.populationSize) {
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
                ?: throw RouteNotFoundException("Generic algorithm did not found the best route")
            worstResult = population.maxBy { fitness(it) }
                ?: throw RouteNotFoundException("Generic algorithm did not found the worst route")
            meanFitness = population.sumByDouble { fitness(it).toDouble() } / population.size
            detailLogger?.logLine("$generationNo; ${fitness(bestResult)}; ${fitness(worstResult)}; $meanFitness")
        }
        return bestResult
    }
}
