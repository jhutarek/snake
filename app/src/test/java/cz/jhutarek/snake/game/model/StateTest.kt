package cz.jhutarek.snake.game.model

import cz.jhutarek.snake.game.testinfrastructure.CustomStringSpec
import io.kotlintest.shouldBe
import io.mockk.every
import io.mockk.mockk

internal class StateTest : CustomStringSpec({

    "score in running state should be multiply of eaten apples" {
        val applesEaten = 123
        val dimensions = mockk<Dimensions>()
        val snake = mockk<Snake> {
            every { this@mockk.applesEaten } returns applesEaten
        }
        val apples = mockk<Apples>()

        State.Running(dimensions, snake, apples).score shouldBe 12_300
    }
})