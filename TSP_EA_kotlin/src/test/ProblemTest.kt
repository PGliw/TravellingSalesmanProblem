package test

import mian.Problem
import org.junit.Test
import kotlin.random.Random

internal class ProblemTest {
    val problem = Problem("C:\\Users\\Piotr\\jvm-workspace\\untitled\\TSP_EA_kotlin\\berlin11_modified.tsp", Random(10))

    @Test
    fun geneticTest(){
        val result = problem.genetic(6, 0.5f, 0.8f, 200, 4)
    }
}