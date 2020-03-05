// 1. Loading data + fitness fun (1 pkt.)
// 2. Random method (1 pkt.)
// 3. Alg. zachłanny

// Optymalizacja - macierz odległości między miastami

fun printCities(cities: List<City>){
    for ((index, city) in cities.withIndex()) {
        println(city)
        println("|  " + city.distTo(cities[(index + 1) % cities.size]))
    }
}

fun main() {
    val cities = loadData("C:\\Users\\Piotr\\jvm-workspace\\untitled\\TSP_EA_kotlin\\berlin11_modified.tsp")
    println("---------Cities---------")
    printCities(cities)
    println("------------------------\n")

    val greedyRoute = greedyAlgorithm(cities)
    println("---------Greedy---------")
    printCities(greedyRoute)
    println("------------------------")
    println("Greedy fitness: ${fitness(greedyRoute)}")
    println("------------------------\n")

    val randomRoute = randomAlgorithm(cities, 10)
    println("---------Random---------")
    printCities(randomRoute)
    println("------------------------")
    println("Random fitness: ${fitness(randomRoute)}")
    println("------------------------")
}