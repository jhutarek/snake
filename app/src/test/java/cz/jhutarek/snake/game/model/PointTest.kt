package cz.jhutarek.snake.game.model

import cz.jhutarek.snake.game.testinfrastructure.CustomStringSpec
import io.kotlintest.data.forall
import io.kotlintest.shouldBe
import io.kotlintest.tables.row

internal class PointTest : CustomStringSpec({

    "minus operator should return correct point" {
        forall(
            row(Point(0f, 0f), Point(0f, 0f), Point(0f, 0f)),
            row(Point(0f, 0f), Point(5f, -4f), Point(5f, -4f)),
            row(Point(-5f, 10f), Point(-2f, 4f), Point(3f, -6f)),
            row(Point(5f, -10f), Point(3f, -6f), Point(-2f, 4f))
        ) { expected, first, second ->
            first - second shouldBe expected
        }
    }

    "abs should return point with coordinates which are absolute values of original coordinates" {
        forall(
            row(Point(0f, 0f), Point(0f, 0f)),
            row(Point(1f, 1f), Point(1f, 1f)),
            row(Point(1f, 1f), Point(1f, -1f)),
            row(Point(1f, 1f), Point(-1f, 1f)),
            row(Point(1f, 1f), Point(-1f, -1f))
        ) { expected, original ->
            abs(original) shouldBe expected
        }
    }
})