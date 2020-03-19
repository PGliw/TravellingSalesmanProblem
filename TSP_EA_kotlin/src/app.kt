import java.util.*

// 1. Loading data + fitness fun (1 pkt.)
// 2. Random method (1 pkt.)
// 3. Alg. zachłanny

// Optymalizacja - macierz odległości między miastami

fun printCities(cities: List<City>) {
    for ((index, city) in cities.withIndex()) {
        println(city)
        println("|  " + city.distTo(cities[(index + 1) % cities.size]))
    }
}

fun main() {
    // fine: 1, 2
    // fine, but slow: 3
    // too slow or not fine: 0
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

    val problem = Problem("C:\\Users\\Piotr\\jvm-workspace\\untitled\\TSP_EA_kotlin\\${filenames[3]}")

    val greedyRoute = problem.greedy()
    println("---------Greedy route---------")
    printCities(greedyRoute)
    println("------------------------")
    println("Greedy fitness: ${fitness(greedyRoute)}")
    println("------------------------\n")

    val randomRoute = problem.random(10)
    println("---------Random problem---------")
    printCities(randomRoute)
    println("------------------------")
    println("Random fitness: ${fitness(randomRoute)}")
    println("------------------------")

}