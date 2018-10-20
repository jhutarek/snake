package cz.jhutarek.snake.game.domain

import cz.jhutarek.snake.game.model.*
import cz.jhutarek.snake.game.model.Direction.*
import cz.jhutarek.snake.game.testinfrastructure.CustomStringSpec
import io.kotlintest.matchers.types.shouldBeInstanceOf
import io.kotlintest.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifyOrder

internal class StateUpdaterTest : CustomStringSpec({

    val dimensions = Dimensions(30, 20)
    val newSnake = Snake(cells = (0..2).map { Cell(it, 0) }, direction = LEFT)
    val newApples = mockk<Apples>()
    val snake = spyk(Snake(cells = (1..3).map { Cell(it, 0) }, direction = LEFT)) {
        every { move() } returns this
        every { turn(any()) } returns this
        every { eat(any()) } returns Pair(newSnake, newApples)
    }
    val apples = mockk<Apples> {
        every { grow() } returns newApples
    }
    val runningState = State.Running(dimensions, snake, apples)
    val direction = RIGHT

    val updater = StateUpdater(runningState)

    "updater should update from waiting state to given first running state" {
        updater.update(State.Waiting, direction) shouldBe runningState
    }

    "snake should turn, move and eat grown apples on update in running state" {
        updater.update(runningState, direction)

        verifyOrder {
            snake.turn(direction)
            snake.move()
            snake.eat(newApples)
        }
    }

    "updater should return running state with new snake and apples when updating from running state" {
        updater.update(runningState, direction) shouldBe runningState.copy(snake = newSnake, apples = newApples)
    }

    fun testOverState(cells: List<Cell>, direction: Direction) {
        val stateBeforeDeath = State.Running(
            dimensions,
            Snake(cells = cells, direction = direction),
            Apples(field = dimensions)
        )

        updater.update(stateBeforeDeath, direction).shouldBeInstanceOf<State.Over>()
    }

    "updater should return over state if snake eats itself" {
        testOverState(listOf(Cell(0, 1), Cell(0, 0), Cell(1, 0), Cell(1, 1), Cell(1, 2)), RIGHT)
    }

    "updater should return over state if snake runs into top wall" {
        testOverState(listOf(Cell(1, 0), Cell(1, 1)), UP)
    }

    "updater should return over state if snake runs into left wall" {
        testOverState(listOf(Cell(0, 1), Cell(1, 1)), LEFT)
    }

    "updater should return over state if snake runs into bottom wall" {
        testOverState(listOf(Cell(1, dimensions.height - 1), Cell(1, dimensions.height - 2)), DOWN)
    }

    "updater should return over state if snake runs into right wall" {
        testOverState(listOf(Cell(dimensions.width - 1, 1), Cell(dimensions.width - 2, 1)), RIGHT)
    }

    "updater should return over state with correct score after death" {
        val stateBeforeDeath = State.Running(
            dimensions,
            Snake(cells = listOf(Cell(1, 0), Cell(1, 1)), direction = UP, applesEaten = 456),
            Apples(field = dimensions)
        )

        (updater.update(stateBeforeDeath, UP) as State.Over).score shouldBe stateBeforeDeath.score
    }

    "updater should do nothing on update from over state" {
        val anyOverState = State.Over(123)

        updater.update(anyOverState, direction) shouldBe anyOverState
    }
})