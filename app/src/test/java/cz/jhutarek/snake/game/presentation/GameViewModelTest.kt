package cz.jhutarek.snake.game.presentation

import cz.jhutarek.snake.game.domain.Game
import cz.jhutarek.snake.game.model.Direction.*
import cz.jhutarek.snake.game.model.Point
import cz.jhutarek.snake.game.testinfrastructure.CustomStringSpec
import io.kotlintest.data.forall
import io.kotlintest.tables.row
import io.mockk.clearMocks
import io.mockk.mockk
import io.mockk.verify

internal class GameViewModelTest : CustomStringSpec({

    val game = mockk<Game>(relaxUnitFun = true)

    val viewModel = GameViewModel(game)

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
            row(RIGHT, Point(0, 0), Point(1, 0)),
            row(LEFT, Point(0, 0), Point(-1, 0)),
            row(UP, Point(0, 0), Point(0, -1)),
            row(DOWN, Point(0, 0), Point(0, 1)),

            row(RIGHT, Point(-1, -1), Point(5, -2)),
            row(UP, Point(-1, -1), Point(2, -5)),
            row(UP, Point(-1, -1), Point(-2, -5)),
            row(LEFT, Point(-1, -1), Point(-5, -2)),
            row(LEFT, Point(-1, -1), Point(-5, 2)),
            row(DOWN, Point(-1, -1), Point(-2, 5)),
            row(DOWN, Point(-1, -1), Point(2, 5)),
            row(RIGHT, Point(-1, -1), Point(5, 2))
        ) { expectedDirection, from, to ->
            clearMocks(game)

            viewModel.swipe(from, to)

            verify { game.direction = expectedDirection }
        }
    }
})