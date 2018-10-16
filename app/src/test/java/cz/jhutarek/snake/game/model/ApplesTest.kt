package cz.jhutarek.snake.game.model

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import java.util.stream.Stream
import kotlin.random.Random

internal class ApplesTest {

    @Nested
    inner class ParameterChecksTest {
        private val expectedDefaultMaxApples = 3
        private val expectedDefaultGrowthProbability = 0.1

        private val anyField = Dimensions(15, 10)

        @Test
        fun `default cells should be empty`() {
            assertThat(Apples(field = anyField).cells)
                .isEmpty()
        }

        @Test
        fun `default max apples should have correct value`() {
            assertThat(Apples(field = anyField).maxApples)
                .isEqualTo(expectedDefaultMaxApples)
        }

        @ParameterizedTest
        @ValueSource(ints = [-10, -1, 0])
        fun `should require max apples to be positive`(maxApples: Int) {
            assertThatThrownBy { Apples(field = anyField, maxApples = maxApples) }
                .isInstanceOf(IllegalArgumentException::class.java)
        }

        @Test
        fun `default growth probability should have correct value`() {
            assertThat(Apples(field = anyField).growthProbability)
                .isEqualTo(expectedDefaultGrowthProbability)
        }

        @ParameterizedTest
        @ValueSource(doubles = [-10.0, -1.0, -0.0001, 1.00001, 10.0])
        fun `should require growth probability to be in 0-1`(growthProbability: Double) {
            assertThatThrownBy { Apples(field = anyField, growthProbability = growthProbability) }
                .isInstanceOf(IllegalArgumentException::class.java)
        }
    }

    @Nested
    inner class GrowTest() {

        private val anyField = Dimensions(20, 30)
        private val anyGrowthProbability = 0.5
        private val existingAppleCells = setOf(Cell(1, 2))
        private val expectedNewAppleCell = Cell(15, 16)
        private val randomGenerator = mockk<Random>().apply {
            every { nextInt(anyField.width) } returns expectedNewAppleCell.x
            every { nextInt(anyField.height) } returns expectedNewAppleCell.y
        }

        @ParameterizedTest
        @MethodSource("growthProvider")
        fun `should grow apples only when generated random number is below growth probability threshold and there is room for more apples`(
            expectedAppleCells: Set<Cell>,
            generatedRandomNumber: Double,
            maxApples: Int
        ) {
            every { randomGenerator.nextDouble() } returns generatedRandomNumber
            val apples = Apples(
                field = anyField,
                cells = existingAppleCells,
                maxApples = maxApples,
                growthProbability = anyGrowthProbability,
                randomGenerator = randomGenerator
            )

            assertThat(apples.grow())
                .isEqualTo(apples.copy(cells = expectedAppleCells))
        }

        private fun growthProvider(): Stream<Arguments> {
            val generatedNumberBelowThreshold = anyGrowthProbability - 0.1
            val generatedNumberAboveThreshold = anyGrowthProbability + 0.1
            val maxApplesWithRoom = existingAppleCells.size + 1
            val maxApplesWithoutRoom = existingAppleCells.size

            return Stream.of(
                arguments(
                    existingAppleCells + expectedNewAppleCell,
                    generatedNumberBelowThreshold,
                    maxApplesWithRoom
                ),
                arguments(
                    existingAppleCells,
                    generatedNumberBelowThreshold,
                    maxApplesWithoutRoom
                ),
                arguments(
                    existingAppleCells,
                    generatedNumberAboveThreshold,
                    maxApplesWithRoom
                ),
                arguments(
                    existingAppleCells,
                    generatedNumberAboveThreshold,
                    maxApplesWithoutRoom
                )
            )
        }
    }
}

/*@ParameterizedTest
@Test
fun `should grow new apple only when `*/

/*@Test
fun `should not grow new apple if random number is above growth probability threshold, even if there is room for more apples`() {
    val randomGenerator = mockk<Random>().apply {
        every { nextDouble() } returns 0.6
    }
    val apples = Apples(
        field = anyField,
        cells = emptySet(),
        maxApples = 100,
        growthProbability = 0.5,
        randomGenerator = randomGenerator
    )

    assertThat(apples.grow())
        .isEqualTo(apples)
}*/

/*@Test
fun `should grow new apple at random cell when random number fits and there is room for mode apples`() {
    val growthProbability = 0.5
    val fittingGrowthRandom = 0.4
    val randomCellX = 1
    val randomCellY = 2
    val randomGenerator = mockk<Random>().apply {
        every { nextDouble() } returns fittingGrowthRandom
        every { nextInt(anyField.width) } returns randomCellX
        every { nextInt(anyField.height) } returns randomCellY
    }

    Apples(
        field = anyField,
        growthProbability = growthProbability,
        maxApples = 2,
        cells = setOf(Cell(0, 0)),
        randomGenerator = randomGenerator
    )
}*/

