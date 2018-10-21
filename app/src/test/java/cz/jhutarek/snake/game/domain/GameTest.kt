package cz.jhutarek.snake.game.domain

import cz.jhutarek.snake.game.domain.Vibrator.Pattern.APPLE_EATEN
import cz.jhutarek.snake.game.domain.Vibrator.Pattern.SNAKE_DIED
import cz.jhutarek.snake.game.model.*
import cz.jhutarek.snake.game.model.Direction.DOWN
import cz.jhutarek.snake.game.model.Direction.UP
import cz.jhutarek.snake.game.testinfrastructure.CustomStringSpec
import io.kotlintest.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify

internal class GameTest : CustomStringSpec({

    val interval = 1234L
    val tickerListenerSlot = slot<TickerListener>()
    val ticker = mockk<Ticker>(relaxUnitFun = true) {
        every { listener = capture(tickerListenerSlot) } returns Unit
    }
    val vibrator = mockk<Vibrator>(relaxUnitFun = true)
    val snake = Snake(listOf(Cell(1, 1)), direction = UP)
    val apples = mockk<Apples>()
    val listener = mockk<GameListener> {
        every { this@mockk(any()) } returns Unit
    }
    val firstRunningState = State.Running(Dimensions(10, 15), snake, apples)
    val otherState = State.Over(123)
    val stateUpdater = mockk<StateUpdater> {
        every { this@mockk.firstRunningState } returns firstRunningState
        every { update(any(), any()) } returns otherState
    }

    val game = Game(stateUpdater, ticker, interval, vibrator).apply { this@apply.listener = listener }

    "game should register ticker listener in constructor" {
        verify { ticker.listener = any() }
    }

    "default direction should be direction from state updater" {
        game.direction shouldBe snake.direction
    }

    "game should reset the direction to default on start" {
        game.direction = DOWN

        game.start()

        game.direction shouldBe snake.direction
    }

    "game should start ticker with correct interval on start" {
        game.start()

        verify { ticker.start(interval) }
    }

    "game should notify listener with waiting state when added" {
        verify { listener(State.Waiting) }
    }

    "game should notify listener with waiting state on reset" {
        game.reset()

        verify { listener(State.Waiting) }
    }

    "game should stop ticker on reset" {
        game.reset()

        verify { ticker.stop() }
    }

    "game should request state update with latest direction on ticker listener invocation" {
        game.direction = DOWN

        tickerListenerSlot.captured(Unit)

        verify { stateUpdater.update(State.Waiting, DOWN) }
    }

    "game should notify listener with next state on ticker listener invocation" {
        tickerListenerSlot.captured(Unit)

        verify { listener(otherState) }
    }

    "game should stop ticker if next state on ticker listener invocation is over state" {
        tickerListenerSlot.captured(Unit)

        verify { ticker.stop() }
    }

    "game should vibrate if the snake has eaten an apple" {
        every { stateUpdater.update(any(), any()) } returns firstRunningState.copy(snake = snake.copy(futureGrowth = 1))

        tickerListenerSlot.captured(Unit)

        verify { vibrator.vibrate(APPLE_EATEN) }
    }

    "game should vibrate if the snake has died" {
        every { stateUpdater.update(any(), any()) } returns State.Over(123)

        tickerListenerSlot.captured(Unit)

        verify { vibrator.vibrate(SNAKE_DIED) }
    }
})