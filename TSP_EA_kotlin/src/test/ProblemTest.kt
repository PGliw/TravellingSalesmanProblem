package test

import main.problem.traditional.RandomProblem
import org.junit.Test

internal class ProblemTest {
    private val problem =
        RandomProblem(
            "C:\\Users\\Piotr\\jvm-workspace\\untitled\\TSP_EA_kotlin\\berlin11_modified.tsp",
            5
        )

    @Test
    fun geneticTest() {
        val result = problem.solve()
    }
}