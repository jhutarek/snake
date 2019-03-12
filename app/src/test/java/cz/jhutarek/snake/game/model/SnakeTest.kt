package cz.jhutarek.snake.game.model

import cz.jhutarek.snake.game.model.Direction.*
import cz.jhutarek.snake.game.testinfrastructure.CustomStringSpec
import io.kotlintest.data.forall
import io.kotlintest.matchers.collections.shouldContainExactly
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.tables.row

internal class SnakeTest : CustomStringSpec({

    val direction = LEFT
    val headCell = Cell(3, 3)
    val tailCells = listOf(Cell(4, 3), Cell(5, 3))
    val snakeCells = headCell + tailCells
    val snake = Snake(snakeCells, direction)
    val apples = Apples(field = Dimensions(20, 20))

    "constructor should require non empty cells" {
        shouldThrow<IllegalArgumentException> { Snake(listOf(), direction) }
    }

    "snake should return first cell as head" {
        snake.head shouldBe headCell
    }

    "snake should return all cells except first as tail" {
        snake.tail shouldContainExactly tailCells
    }

    "snake should require direction to not reverse it" {
        shouldThrow<IllegalArgumentException> { Snake(snakeCells, RIGHT) }
    }

    "snake should require apples eaten not to be negative" {
        forall(
            row(-10),
            row(-1)
        ) { applesEaten ->
            shouldThrow<IllegalArgumentException> { Snake(snakeCells, LEFT, applesEaten = applesEaten) }
        }
    }

    "snake should require future growth not to be negative" {
        forall(
            row(-10),
            row(-1)
        ) { futureGrowth ->
            shouldThrow<IllegalArgumentException> { Snake(snakeCells, LEFT, futureGrowth = futureGrowth) }
        }
    }

    "snake should move by one cell in given direction and keep the direction" {
        forall(
            row(Snake(listOf(Cell(2, 3), Cell(3, 3), Cell(4, 3)), LEFT), LEFT),
            row(Snake(listOf(Cell(3, 2), Cell(3, 3), Cell(4, 3)), UP), UP),
            row(Snake(listOf(Cell(3, 4), Cell(3, 3), Cell(4, 3)), DOWN), DOWN)
        ) { expectedSnake, direction ->
            Snake(snakeCells, direction).move() shouldBe expectedSnake
        }
    }

    "snake should turn if new direction is perpendicular to the old one" {
        forall(
            row(LEFT, LEFT, RIGHT),
            row(UP, LEFT, UP),
            row(DOWN, LEFT, DOWN),
            row(LEFT, LEFT, LEFT)
        ) { expectedSnakeDirection, originalSnakeDirection, direction ->
            Snake(snakeCells, originalSnakeDirection).turn(direction) shouldBe Snake(snakeCells, expectedSnakeDirection)
        }
    }

    "snake should be the same after eating no apples" {
        snake.eat(apples) shouldBe Snake.EatingResult(snake, apples)
    }

    "snake should return new snake with apples eaten and future growth and apples without eaten apple if apple was eaten" {
        val applesToEat = apples.copy(cells = setOf(headCell, Cell(0, 0)))

        snake.eat(applesToEat) shouldBe Snake.EatingResult(
            snake.copy(futureGrowth = 1, applesEaten = 1),
            applesToEat.copy(cells = setOf(Cell(0, 0)))
        )
    }

    "snake should grow tail on next move if future growth is greater than zero" {
        snake.copy(futureGrowth = 1).move() shouldBe
                snake.copy(cells = listOf(Cell(2, 3), Cell(3, 3), Cell(4, 3), Cell(5, 3)), futureGrowth = 0)
    }
})