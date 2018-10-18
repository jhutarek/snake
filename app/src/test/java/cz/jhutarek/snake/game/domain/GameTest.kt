package cz.jhutarek.snake.game.domain

import cz.jhutarek.snake.game.model.*
import cz.jhutarek.snake.game.model.Direction.DOWN
import cz.jhutarek.snake.game.model.Direction.UP
import io.mockk.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class GameTest {

    private val expectedIntervalMillis = 150L
    private val anySnake = Snake(listOf(Cell(1, 1)), direction = UP)
    private val anyApples = mockk<Apples>()
    private val anyFirstRunningState = State.Running(Dimensions(10, 15), anySnake, anyApples)
    private val anyOtherState = State.Over(123)
    private val anyStateUpdater = mockk<StateUpdater> {
        every { firstRunningState } returns anyFirstRunningState
        every { update(any(), any()) } returns anyOtherState
    }
    private val tickerListenerSlot = slot<TickerListener>()
    private val anyTicker = mockk<Ticker>(relaxUnitFun = true) {
        every { listener = capture(tickerListenerSlot) } returns Unit
    }
    private val anyListener = mockk<GameListener> {
        every { this@mockk.invoke(any()) } returns Unit
    }

    private val game = Game(anyStateUpdater, anyTicker).apply {
        listener = anyListener
    }

    @Test
    fun `should register ticker listener on construction`() {
        verify { anyTicker.listener = any() }
    }

    @Test
    fun `default direction should be direction from state updater`() {
        assertThat(Game(anyStateUpdater, anyTicker).direction)
            .isEqualTo(anySnake.direction)
    }

    @Test
    fun `should start ticker with correct interval on start`() {
        game.start()

        verify { anyTicker.start(expectedIntervalMillis) }
    }

    @Test
    fun `should notify listener with waiting state when added`() {
        verify { anyListener(State.Waiting) }
    }

    @Test
    fun `should notify with waiting state on reset`() {
        clearMocks(anyListener, answers = false)

        game.reset()

        verify { anyListener(State.Waiting) }
    }

    @Test
    fun `should stop ticker on reset`() {
        game.reset()

        verify { anyTicker.stop() }
    }

    @Test
    fun `should request state update with latest direction on ticker listener invocation`() {
        game.direction = DOWN

        tickerListenerSlot.captured.invoke(Unit)

        verify { anyStateUpdater.update(State.Waiting, DOWN) }
    }

    @Test
    fun `should notify game listener with next state on ticker listener invocation`() {
        tickerListenerSlot.captured.invoke(Unit)

        verify { anyListener(anyOtherState) }
    }

    @Test
    fun `should stop ticker if next state on ticker listener invocation is over`() {
        tickerListenerSlot.captured.invoke(Unit)

        verify { anyTicker.stop() }
    }
}