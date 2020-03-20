package test

import mian.*
import org.junit.Before
import org.junit.Test
import kotlin.random.Random
import kotlin.test.assertEquals

internal class OperatorAlgorithmsKtTest {

    private var random = Random(7)
    private val cities = loadData("C:\\Users\\Piotr\\jvm-workspace\\untitled\\TSP_EA_kotlin\\berlin11_modified.tsp")
    private val randomRoutes = initialize(cities, 10, random)

    @Before
    fun resetRandom() {
        random = Random(5)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `initialize should throw IllegalArgumetException`(){
        val result = initialize(cities, -1, random)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `roulette selection should throw  IllegalArgumentException`() {
        val result = rouletteSelection(randomRoutes.toList(), 5.0, random)
    }

    @Test
    fun `roulette selection should return route #7`() {
        val result = rouletteSelection(randomRoutes.toList(), 0.1, random)
        assertEquals(randomRoutes.toList()[7], result)
    }

    @Test
    fun `tournamentSelection should return route #6`() {
        val result = tournamentSelection(randomRoutes.toList(), 6, random)
        assertEquals(randomRoutes.toList()[6], result)
    }

    @Test
    fun `orderedCrossOver should return child (2, 6, 4, 8, 10, 3, 9, 11, 7, 1, 5)`() {
        val result = orderedCrossOver(randomRoutes.toList()[8], randomRoutes.toList()[2], Random(56))
        val expected = listOf(
            cities[1],
            cities[5],
            cities[3],
            cities[7],
            cities[9],
            cities[2],
            cities[8],
            cities[10],
            cities[6],
            cities[0],
            cities[4]
        )
        assertEquals(expected, result)
    }

    @Test
    fun `orderedCrossOver should return child` () {
        val result = orderedCrossOver(randomRoutes.toList()[3], randomRoutes.toList()[4], random)
        val expected = listOf(
            cities[4],
            cities[6],
            cities[3],
            cities[7],
            cities[10],
            cities[5],
            cities[9],
            cities[1],
            cities[2],
            cities[0],
            cities[8]
        )
        assertEquals(expected, result)
    }

    @Test
    fun `orderChangingMutation should return (3, 4, 9, 10, 2, 11, 5, 1, 8, 6, 7)`() {
        val result = orderChangingMutation(randomRoutes.toList()[7], random)
    }

    @Test
    fun `orderChangingMutation should return (6, 4, 10, 5, 9, 11, 2, 8, 7, 1, 3)`() {
        val result = orderChangingMutation(randomRoutes.toList()[9], Random(73))
    }
}