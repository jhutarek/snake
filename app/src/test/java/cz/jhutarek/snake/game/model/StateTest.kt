package cz.jhutarek.snake.game.model

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class StateTest {

    private val anyApplesEaten = 123
    private val anyDimensions = mockk<Dimensions>()
    private val anySnake = mockk<Snake> {
        every { applesEaten } returns anyApplesEaten
    }
    private val anyApples = mockk<Apples>()

    @Test
    fun `score in running state should be multiply of eaten apples`() {
        assertThat(State.Running(anyDimensions, anySnake, anyApples).score)
            .isEqualTo(12_300)
    }
}