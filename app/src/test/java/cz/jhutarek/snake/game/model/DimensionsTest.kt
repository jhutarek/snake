package cz.jhutarek.snake.game.model

import cz.jhutarek.snake.game.testinfrastructure.CustomStringSpec
import io.kotlintest.data.forall
import io.kotlintest.shouldThrow
import io.kotlintest.tables.row

internal class DimensionsTest : CustomStringSpec({

    "constructor should require both dimensions to be positive" {
        forall(
            row(-10, 1),
            row(-1, 1),
            row(0, 1),
            row(1, -10),
            row(1, -1),
            row(1, 0),
            row(0, 0)
        ) { width, height ->
            shouldThrow<IllegalArgumentException> { Dimensions(width, height) }
        }
    }
})