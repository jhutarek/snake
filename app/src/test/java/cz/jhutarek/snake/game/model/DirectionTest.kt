package cz.jhutarek.snake.game.model

import cz.jhutarek.snake.game.model.Direction.*
import cz.jhutarek.snake.game.testinfrastructure.CustomStringSpec
import io.kotlintest.data.forall
import io.kotlintest.shouldBe
import io.kotlintest.tables.row

internal class DirectionTest : CustomStringSpec({

    "direction should have correct dx and dy" {
        forall(
            row(0, -1, UP),
            row(0, 1, DOWN),
            row(-1, 0, LEFT),
            row(1, 0, RIGHT)
        ) { expectedDx, expectedDy, direction ->
            direction.dx shouldBe expectedDx
            direction.dy shouldBe expectedDy
        }
    }

    "direction should return correct perpendicular result" {
        forall(
            row(false, UP, DOWN),
            row(false, DOWN, UP),
            row(false, LEFT, RIGHT),
            row(false, RIGHT, LEFT),
            row(false, UP, UP),
            row(false, RIGHT, RIGHT),
            row(false, LEFT, LEFT),
            row(false, DOWN, DOWN),
            row(true, UP, LEFT),
            row(true, LEFT, UP),
            row(true, LEFT, DOWN),
            row(true, DOWN, LEFT),
            row(true, DOWN, RIGHT),
            row(true, RIGHT, DOWN),
            row(true, RIGHT, UP),
            row(true, UP, RIGHT)
        ) { expectedIsPerpendicular, direction1, direction2 ->
            direction1 isPerpendicularTo direction2 shouldBe expectedIsPerpendicular
        }
    }
})