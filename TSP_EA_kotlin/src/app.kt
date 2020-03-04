import java.io.File

/**
 * @param filePath path to file with list of cities
 * @return list of Cities
 */
fun loadData(filePath: String): List<City> {
    val lines = File(filePath).readLines()
    val digitLines = lines.filter { line -> line.firstOrNull()?.isDigit() ?: false }
    val splitLines = digitLines.map { it.split(" ") }
    return splitLines.map { list ->
        if(list.size < 3) throw DataFileFormatException("There are only ${list.size} columns")
        val id = list[0].toIntOrNull() ?: throw DataFileFormatException("First column - ${list[0]} - is not an integer")
        val x = list[1].toFloatOrNull() ?: throw DataFileFormatException("Second column - ${list[1]} - is not a float")
        val y = list[2].toFloatOrNull() ?: throw DataFileFormatException("Third column - ${list[2]} - is not a float")
        City(id, x, y)
    }
}

class DataFileFormatException(message: String) : RuntimeException(message)

data class City(val id: Int, val x:Float, val y: Float)

fun main(){
    val cities = loadData("C:\\Users\\Piotr\\jvm-workspace\\untitled\\TSP_EA_kotlin\\berlin11_modified.tsp")
    cities.forEach { println(it) }
}