package cz.jhutarek.snake.game.domain

import cz.jhutarek.snake.game.model.Direction
import cz.jhutarek.snake.game.model.State
import javax.inject.Inject
import javax.inject.Singleton

typealias GameListener = (State) -> Unit

@Singleton
class Game @Inject constructor(
    private val stateUpdater: StateUpdater,
    private val ticker: Ticker
) {
    init {
        ticker.listener = { update() }
    }

    companion object {
        private const val TICKER_INTERVAL_MILLIS = 150L
    }

    private var state: State = State.Waiting
        set(value) {
            field = value
            listener?.invoke(field)
        }

    var listener: GameListener? = null
        set(value) {
            field = value
            field?.invoke(state)
        }

    var direction: Direction = stateUpdater.firstRunningState.snake.direction

    fun reset() {
        ticker.stop()
        state = State.Waiting
    }

    fun start() {
        ticker.start(TICKER_INTERVAL_MILLIS)
    }

    private fun update() {
        state = stateUpdater.update(state, direction)

        if (state is State.Over) {
            ticker.stop()
        }
    }
}