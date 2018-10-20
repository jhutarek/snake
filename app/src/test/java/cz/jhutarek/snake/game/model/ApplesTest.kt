package cz.jhutarek.snake.game.model

import cz.jhutarek.snake.game.testinfrastructure.CustomStringSpec
import io.kotlintest.data.forall
import io.kotlintest.matchers.beEmpty
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.tables.row
import io.mockk.every
import io.mockk.mockk
import kotlin.random.Random

internal class ApplesTest : CustomStringSpec({

    val field = Dimensions(15, 10)
    val apples = Apples(field = field)

    "default cells should be empty" {
        apples.cells should beEmpty()
    }

    "constructor should require max apples to be positive" {
        forall(
            row(-10),
            row(-1),
            row(0)
        ) { maxApples ->
            shouldThrow<IllegalArgumentException> { Apples(field = field, maxApples = maxApples) }
        }
    }

    "constructor should require growth probability to be in [0, 1)" {
        forall(
            row(-10.0),
            row(-1.0),
            row(-0.0001),
            row(1.00001),
            row(10.0)
        ) { growthProbability ->
            shouldThrow<IllegalArgumentException> { Apples(field = field, growthProbability = growthProbability) }
        }
    }

    "should grow apples only when generated random number is below growth probability threshold and there is room for more apples" {
        val currentApplesCells = setOf(Cell(1, 2))
        val expectedNewAppleCell = Cell(15, 16)
        val randomGenerator = mockk<Random> {
            every { nextInt(field.width) } returns expectedNewAppleCell.x
            every { nextInt(field.height) } returns expectedNewAppleCell.y
        }
        val growthProbability = 0.5
        val generatedNumberBelowThreshold = growthProbability - 0.1
        val generatedNumberAboveThreshold = growthProbability + 0.1
        val maxApplesWithoutRoom = currentApplesCells.size
        val maxApplesWithRoom = currentApplesCells.size + 1

        forall(
            row(
                currentApplesCells + expectedNewAppleCell,
                generatedNumberBelowThreshold,
                maxApplesWithRoom
            ),
            row(
                currentApplesCells,
                generatedNumberBelowThreshold,
                maxApplesWithoutRoom
            ),
            row(
                currentApplesCells,
                generatedNumberAboveThreshold,
                maxApplesWithRoom
            ),
            row(
                currentApplesCells,
                generatedNumberAboveThreshold,
                maxApplesWithoutRoom
            )
        ) { expectedCells, generatedRandomNumber, maxApples ->
            every { randomGenerator.nextDouble() } returns generatedRandomNumber
            val originalApples = apples.copy(
                cells = currentApplesCells,
                maxApples = maxApples,
                growthProbability = growthProbability,
                randomGenerator = randomGenerator
            )

            originalApples.grow() shouldBe originalApples.copy(cells = expectedCells)
        }
    }
})

