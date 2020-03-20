package main.problem

import main.utils.IncompatibleCitiesException
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
            else -> throw IncompatibleCitiesException("Required cityEuc type not found")
        }
    }

    override fun toString() = "City #$id ($x, $y)"
}