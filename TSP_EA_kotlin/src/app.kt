import java.io.File
import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.pow
import kotlin.math.sqrt

// 1. Loading data + fitness fun (1 pkt.)
// 2. Random method (1 pkt.)
// 3. Alg. zachłanny

// Optymalizacja - macierz odległości między miastami


fun main() {
    val cities = loadData("C:\\Users\\Piotr\\jvm-workspace\\untitled\\TSP_EA_kotlin\\berlin11_modified.tsp")
    for ((index, city) in cities.withIndex()) {
        println(city)
        println("|  " + city.distTo(cities[(index + 1) % cities.size]))
    }
}