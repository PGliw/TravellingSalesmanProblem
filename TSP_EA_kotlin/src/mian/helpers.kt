package mian

import java.io.File
import kotlin.random.Random

/**
 * @param filePath path to file with list of cities
 * @return list of Cities
 */
fun loadData(filePath: String): List<City> {
    val lines = File(filePath).readLines()
    val edgeWeightLine = lines.firstOrNull { it.contains("EDGE_WEIGHT_TYPE") }?.split(" ")
        ?: throw  DataFileFormatException("The file does not contain correct edge weight type info")
    if (edgeWeightLine.size < 2) throw DataFileFormatException("The file type info is incorrectly formatted")
    val digitLines = lines.filter { line -> line.firstOrNull()?.isDigit() ?: false }
    val splitLines = digitLines.map { line -> line.split(" ").filter { it.isNotEmpty() } }
    return splitLines.map { list ->
        if (list.size < 3) throw DataFileFormatException("There are only ${list.size} columns")
        val id = list[0].toIntOrNull() ?: throw DataFileFormatException("First column - ${list[0]} - is not an integer")
        val x = list[1].toFloatOrNull() ?: throw DataFileFormatException("Second column - ${list[1]} - is not a float")
        val y = list[2].toFloatOrNull() ?: throw DataFileFormatException("Third column - ${list[2]} - is not a float")
        when (val edgeType = edgeWeightLine.last()) {
            "EUC_2D" -> City.CityEuc(id, x, y)
            "GEO" -> City.CityGeo(id, x, y)
            else -> throw DataFileFormatException("Unknown edge weight type: $edgeType")
        }
    }
}

/**
 * @param route list of ordered cities
 * @param distanceFunction lets change default distance calculation method
 * @return route length
 */
fun fitness(
    route: List<City>,
    distanceFunction: (City, City) -> Float = { city1: City, city2: City -> city1.distTo(city2) }
): Float {
    var result = 0f
    for ((index, city) in route.withIndex()) {
        val nextIndex = (index + 1) % route.size
        result += distanceFunction(city, route[nextIndex])
    }
    return result
}

/**
 * @param cities list of cities to be shuffled
 * @return random sequence of cities
 */
fun randomRoute(cities: List<City>, random: Random = Random) = cities.shuffled(random)