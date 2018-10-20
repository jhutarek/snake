package cz.jhutarek.snake.game.model

import cz.jhutarek.snake.game.testinfrastructure.CustomStringSpec
import io.kotlintest.data.forall
import io.kotlintest.shouldBe
import io.kotlintest.tables.row

internal class PointTest : CustomStringSpec({

    "minus operator should return correct point" {
        forall(
            row(Point(0, 0), Point(0, 0), Point(0, 0)),
            row(Point(0, 0), Point(5, -4), Point(5, -4)),
            row(Point(-5, 10), Point(-2, 4), Point(3, -6)),
            row(Point(5, -10), Point(3, -6), Point(-2, 4))
        ) { expected, first, second ->
            first - second shouldBe expected
        }
    }

    "abs should return point with coordinates which are absolute values of original coordinates" {
        forall(
            row(Point(0, 0), Point(0, 0)),
            row(Point(1, 1), Point(1, 1)),
            row(Point(1, 1), Point(1, -1)),
            row(Point(1, 1), Point(-1, 1)),
            row(Point(1, 1), Point(-1, -1))
        ) { expected, original ->
            abs(original) shouldBe expected
        }
    }
})