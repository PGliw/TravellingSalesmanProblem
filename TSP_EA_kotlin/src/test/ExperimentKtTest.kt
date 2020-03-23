package test

import main.problem.std
import org.junit.Test
import kotlin.test.assertEquals

internal class ExperimentKtTest {

    @Test
    fun `standard deviation should be around 4`() {
        val result = arrayOf(-5, 1, 8, 7, 2).map { it.toFloat() }.std()
        assertEquals(4, result.toInt())
    }

    @Test
    fun `standard deviation should be around 1`() {
        val result = arrayOf(1, 4, 3, 2, 2, 4).map { it.toFloat() }.std()
        assertEquals(1, result.toInt())
    }
}