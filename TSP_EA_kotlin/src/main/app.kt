package main

import main.problem.Route
import main.problem.evolutionary.*
import main.problem.fitness
import main.problem.print
import main.problem.traditional.GreedyProblem
import main.problem.traditional.RandomProblem
import kotlin.random.Random

fun main() {

    val filenames = listOf(
        "ali535.tsp",
        "berlin11_modified.tsp",
        "berlin52.tsp",
        "fl417.tsp",
        "gr666.tsp",
        "kroA100.tsp",
        "kroA150.tsp",
        "kroA200.tsp",
        "nrw1379.tsp",
        "pr2392.tsp"
    )

    val greedyProblem =
        GreedyProblem("C:\\Users\\Piotr\\jvm-workspace\\untitled\\TSP_EA_kotlin\\${filenames[3]}")

    val greedyRoute = greedyProblem.solve()
    println("---------Greedy route---------")
//    greedyRoute.print()
    println("------------------------")
    println("Greedy fitness: ${fitness(greedyRoute)}")
    println("------------------------\n")

    val randomProblem =
        RandomProblem(
            "C:\\Users\\Piotr\\jvm-workspace\\untitled\\TSP_EA_kotlin\\${filenames[3]}",
            10
        )

    val randomRoute = randomProblem.solve()
    println("---------Random problem---------")
//    randomRoute.print()
    println("------------------------")
    println("Random fitness: ${fitness(randomRoute)}")
    println("------------------------\n")

    val random = Random(64)
    val gaProblem = GAProblem(
        "C:\\Users\\Piotr\\jvm-workspace\\untitled\\TSP_EA_kotlin\\${filenames[3]}",
        RandomInitializer(20, random),
        RouletteSelector(0.7, random),
//        TournamentSelector(10),
        OrderedCrosser(random),
        SwapMutator(random),
        0.5f,
        0.6f,
        800
    )

    val gaRoute = gaProblem.solve()
    println("---------GA problem---------")
//    gaRoute.print()
    println("------------------------")
    println("GA fitness: ${fitness(gaRoute)}")
    println("------------------------")
}