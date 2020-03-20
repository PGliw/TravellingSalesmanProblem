package test

import DATASETS_DIR
import main.problem.traditional.RandomProblem
import org.junit.Test

internal class ProblemTest {
    private val problem =
        RandomProblem(
            "${DATASETS_DIR}\\berlin11_modified.tsp",
            5
        )

    @Test
    fun geneticTest() {
        val result = problem.solve()
    }
}