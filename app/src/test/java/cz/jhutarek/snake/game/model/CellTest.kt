package cz.jhutarek.snake.game.model

import cz.jhutarek.snake.game.model.Direction.*
import cz.jhutarek.snake.game.testinfrastructure.CustomStringSpec
import io.kotlintest.data.forall
import io.kotlintest.matchers.collections.shouldContainExactly
import io.kotlintest.shouldBe
import io.kotlintest.tables.row

internal class CellTest : CustomStringSpec({

    "cell should move in given direction" {
        forall(
            row(Cell(4, 3), LEFT),
            row(Cell(6, 3), RIGHT),
            row(Cell(5, 2), UP),
            row(Cell(5, 4), DOWN)
        ) { expected, direction ->
            Cell(5, 3).move(direction) shouldBe expected
        }
    }

    "plus operator should create list with given cells" {
        Cell(0, 0) + listOf(Cell(1, 1), Cell(1, 2)) shouldContainExactly listOf(Cell(0, 0), Cell(1, 1), Cell(1, 2))
    }
})