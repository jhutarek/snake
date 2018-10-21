package cz.jhutarek.snake.game.domain

import cz.jhutarek.snake.game.model.State
import javax.inject.Inject
import javax.inject.Singleton

typealias GameListener = (State) -> Unit

@Singleton
class Game @Inject constructor(
    private val stateUpdater: StateUpdater,
    private val ticker: Ticker,
    private val intervalMillis: Long
) {
    init {
        ticker.listener = { update() }
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

    private val defaultDirection = stateUpdater.firstRunningState.snake.direction

    var direction = defaultDirection

    fun reset() {
        ticker.stop()
        state = State.Waiting
    }

    fun start() {
        direction = defaultDirection
        ticker.start(intervalMillis)
    }

    private fun update() {
        state = stateUpdater.update(state, direction)

        if (state is State.Over) {
            ticker.stop()
        }
    }
}