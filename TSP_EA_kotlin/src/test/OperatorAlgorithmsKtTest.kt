package test

import mian.initialize
import mian.loadData
import mian.rouletteSelection
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
    fun `roulette selection should throw  illegal argument exception`() {
        val result = rouletteSelection(randomRoutes.toList(), 5.0, random)
    }

    @Test
    fun `roulette selection should pass`() {
        val result = rouletteSelection(randomRoutes.toList(), 0.1, random)
        assertEquals(randomRoutes.toList()[7], result)
    }

    @Test
    fun tournamentSelectionTest() {
    }

    @Test
    fun orderedCrossOverTest() {
    }

    @Test
    fun orderChangingMutationTest() {
    }
}