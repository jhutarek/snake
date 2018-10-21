package cz.jhutarek.snake.game.presentation

import cz.jhutarek.snake.game.domain.Game
import cz.jhutarek.snake.game.domain.GameListener
import cz.jhutarek.snake.game.model.*
import cz.jhutarek.snake.game.model.Direction.*
import cz.jhutarek.snake.game.testinfrastructure.CustomStringSpec
import io.kotlintest.data.forall
import io.kotlintest.tables.row
import io.mockk.*

internal class GameViewModelTest : CustomStringSpec({

    val gameListenerSlot = slot<GameListener>()
    val game = mockk<Game>(relaxUnitFun = true) {
        every { listener = capture(gameListenerSlot) } returns Unit
    }
    val listener = mockk<GameViewModelListener> {
        every { this@mockk(any()) } returns Unit
    }

    val viewModel = GameViewModel(game).apply {
        this.listener = listener
    }

    "reset should reset the game" {
        viewModel.reset()

        verify { game.reset() }
    }

    "start should start the game" {
        viewModel.start()

        verify { game.start() }
    }

    "view model should set correct direction on game based on swipe" {
        forall(
            row(RIGHT, Point(0f, 0f), Point(1f, 0f)),
            row(LEFT, Point(0f, 0f), Point(-1f, 0f)),
            row(UP, Point(0f, 0f), Point(0f, -1f)),
            row(DOWN, Point(0f, 0f), Point(0f, 1f)),

            row(RIGHT, Point(-1f, -1f), Point(5f, -2f)),
            row(UP, Point(-1f, -1f), Point(2f, -5f)),
            row(UP, Point(-1f, -1f), Point(-2f, -5f)),
            row(LEFT, Point(-1f, -1f), Point(-5f, -2f)),
            row(LEFT, Point(-1f, -1f), Point(-5f, 2f)),
            row(DOWN, Point(-1f, -1f), Point(-2f, 5f)),
            row(DOWN, Point(-1f, -1f), Point(2f, 5f)),
            row(RIGHT, Point(-1f, -1f), Point(5f, 2f))
        ) { expectedDirection, from, to ->
            clearMocks(game)

            viewModel.swipe(from, to)

            verify { game.direction = expectedDirection }
        }
    }

    "view model should notify listener with default state when added after construction" {
        verify {
            listener(
                GameViewModel.State(
                    introVisible = true,
                    gameVisible = false,
                    overVisible = false,
                    field = null,
                    snake = null,
                    apples = null,
                    score = null
                )
            )
        }
    }

    "view model should register game listener" {
        verify { game.listener = any() }
    }

    "view model should map game state to view model state" {
        val field = Dimensions(15, 16)
        val snake = Snake(listOf(Cell(1, 1), Cell(2, 1), Cell(3, 1)), LEFT, 5, 1)
        val apples = Apples(setOf(Cell(0, 0)), field)
        val runningState = State.Running(
            field = field,
            snake = snake,
            apples = apples
        )
        val overState = State.Over(456)

        forall(
            row(
                GameViewModel.State(
                    introVisible = true,
                    gameVisible = false,
                    overVisible = false,
                    field = null,
                    snake = null,
                    apples = null,
                    score = null
                ),
                State.Waiting
            ),
            row(
                GameViewModel.State(
                    introVisible = false,
                    gameVisible = true,
                    overVisible = false,
                    field = field,
                    snake = snake.cells,
                    apples = apples.cells,
                    score = runningState.score.toString()
                ),
                runningState
            ),
            row(
                GameViewModel.State(
                    introVisible = false,
                    gameVisible = false,
                    overVisible = true,
                    field = null,
                    snake = null,
                    apples = null,
                    score = overState.score.toString()
                ),
                overState
            )
        ) { expectedViewModelState, gameState ->
            clearMocks(listener, answers = false)

            gameListenerSlot.captured(gameState)

            verify { listener(expectedViewModelState) }
        }
    }
})