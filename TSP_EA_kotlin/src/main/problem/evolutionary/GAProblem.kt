package main.problem.evolutionary

import main.utils.RouteNotFoundException
import main.problem.*
import kotlin.random.Random

class GAProblem(
    filePath: String,
    private val initializer: IInitializer,
    private val selector: ISelector,
    private val crosser: ICrosser,
    private val mutator: IMutator,
    private val crossOverProbability: Float,
    private val mutationProbability: Float,
    private val generations: Int,
    random: Random = Random
) :
    Problem(filePath, random) {

    init {
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
//            println("generation #$generationNo: ${bestResult?.map{it.id}}, ${fitnessFun(bestResult!!)}")
//            println("generation #$generationNo - sum: ${population.sumByDouble { fitnessFun(it).toDouble() }}")
        }
        return bestResult ?: throw RouteNotFoundException("Generic algorithm did not found the route")
    }
}