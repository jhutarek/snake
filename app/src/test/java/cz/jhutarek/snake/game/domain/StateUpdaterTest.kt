package cz.jhutarek.snake.game.domain

import cz.jhutarek.snake.game.model.*
import cz.jhutarek.snake.game.model.Direction.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifyOrder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class StateUpdaterTest {

    private val anyDimensions = Dimensions(30, 20)
    private val anyApplesEaten = 123
    private val newSnake = Snake(cells = (0..2).map { Cell(it, 0) }, direction = LEFT)
    private val newApples = mockk<Apples>()
    private val anyApples = mockk<Apples> {
        every { grow() } returns newApples
    }
    private val anySnake = spyk(Snake(cells = (1..3).map { Cell(it, 0) }, direction = LEFT)) {
        every { move() } returns this
        every { turn(any()) } returns this
        every { eat(any()) } returns Pair(newSnake, newApples)
    }
    private val anyDirection = RIGHT
    private val anyRunningState = State.Running(anyDimensions, anySnake, anyApples)

    private val updater = StateUpdater(anyRunningState)

    @Test
    fun `should update from waiting to given first running state`() {
        assertThat(updater.update(State.Waiting, anyDirection))
            .isEqualTo(anyRunningState)
    }

    @Test
    fun `should turn, move and eat grown apples on update in running state`() {
        updater.update(anyRunningState, anyDirection)

        verifyOrder {
            anySnake.turn(anyDirection)
            anySnake.move()
            anySnake.eat(newApples)
        }
    }

    @Test
    fun `should return running state with new snake and apples on update in running state`() {
        assertThat(updater.update(anyRunningState, anyDirection))
            .isEqualTo(anyRunningState.copy(snake = newSnake, apples = newApples))
    }

    @Test
    fun `should return over state if snake eats itself`() =
        testOverState(listOf(Cell(0, 1), Cell(0, 0), Cell(1, 0), Cell(1, 1), Cell(1, 2)), RIGHT)

    @Test
    fun `should return over state if snake runs into top wall`() =
        testOverState(listOf(Cell(1, 0), Cell(1, 1)), UP)

    @Test
    fun `should return over state if snake runs into left wall`() =
        testOverState(listOf(Cell(0, 1), Cell(1, 1)), LEFT)

    @Test
    fun `should return over state if snake runs into bottom wall`() =
        testOverState(listOf(Cell(1, anyDimensions.height - 1), Cell(1, anyDimensions.height - 2)), DOWN)

    @Test
    fun `should return over state if snake runs into right wall`() =
        testOverState(listOf(Cell(anyDimensions.width - 1, 1), Cell(anyDimensions.width - 2, 1)), RIGHT)

    private fun testOverState(cells: List<Cell>, direction: Direction) {
        val stateBeforeDeath = State.Running(
            anyDimensions,
            Snake(cells = cells, direction = direction),
            Apples(field = anyDimensions)
        )

        assertThat(updater.update(stateBeforeDeath, direction))
            .isInstanceOf(State.Over::class.java)
    }

    @Test
    fun `should return over state with correct score after death`() {
        val stateBeforeDeath = State.Running(
            anyDimensions,
            Snake(cells = listOf(Cell(1, 0), Cell(1, 1)), direction = UP, applesEaten = anyApplesEaten),
            Apples(field = anyDimensions)
        )

        assertThat((updater.update(stateBeforeDeath, UP) as State.Over).score)
            .isEqualTo(stateBeforeDeath.score)
    }

    @Test
    fun `should do nothing on update from over state`() {
        val anyOverState = State.Over(123)

        assertThat(updater.update(anyOverState, anyDirection))
            .isEqualTo(anyOverState)
    }
}