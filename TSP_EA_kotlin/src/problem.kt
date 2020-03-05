import java.io.File
import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * @param id city unique identifier
 * @param x 1st coordinate
 * @param y 2nd coordinate
 */
sealed class City(val id: Int, val x: Float, val y: Float) {
    abstract fun distTo(other: City): Float
    class CityGeo(id: Int, x: Float, y: Float) : City(id, x, y) {
        override fun distTo(other: City) = when (other) {
            is CityGeo -> acos((other.x - x) * PI / 180 * (other.y - y) * PI / 180).toFloat()
            else -> throw IncompatibleCitiesException("Required cityGeo type not found")
        }
    }

    class CityEuc(id: Int, x: Float, y: Float) : City(id, x, y) {
        override fun distTo(other: City) = when (other) {
            is CityEuc -> sqrt((x - other.x).pow(2) + (y - other.y).pow(2))
            else -> throw IncompatibleCitiesException("Required cityGeo type not found")
        }
    }
    override fun toString() = "City #$id ($x, $y)"
}

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
    val splitLines = digitLines.map { it.split(" ") }
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
 * @return route length
 */
fun fitness(route: List<City>): Float {
    var result = 0f
    for ((index, city) in route.withIndex()) {
        val nextIndex = (index + 1) % route.size
        result += city.distTo(route[nextIndex])
    }
    return result
}

/**
 * @param cities list of cities to be shuffled
 * @return random sequence of cities
 */
fun randomRoute(cities: List<City>) = cities.shuffled()
