package cz.jhutarek.snake.game.presentation

import cz.jhutarek.snake.game.domain.Game
import cz.jhutarek.snake.game.model.Cell
import cz.jhutarek.snake.game.model.Dimensions
import cz.jhutarek.snake.game.model.Direction.*
import cz.jhutarek.snake.game.model.Point
import cz.jhutarek.snake.game.model.State.*
import cz.jhutarek.snake.game.model.abs
import javax.inject.Inject
import javax.inject.Singleton
import cz.jhutarek.snake.game.model.State as GameState

typealias GameViewModelListener = (GameViewModel.State) -> Unit

@Singleton
class GameViewModel @Inject constructor(
    private val game: Game
) {
    init {
        game.listener = { update(it) }
    }

    data class State(
        val introVisible: Boolean,
        val gameVisible: Boolean,
        val overVisible: Boolean,
        val field: Dimensions?,
        val snake: List<Cell>?,
        val apples: Set<Cell>?,
        val score: Int?
    )

    private var state = State(
        introVisible = true,
        gameVisible = false,
        overVisible = false,
        field = null,
        snake = null,
        apples = null,
        score = null
    )
        set(value) {
            field = value
            listener?.invoke(field)
        }

    var listener: GameViewModelListener? = null
        set(value) {
            field = value
            field?.invoke(state)
        }

    fun reset() {
        game.reset()
    }

    fun start() {
        game.start()
    }

    fun swipe(from: Point, to: Point) {
        val (dx, dy) = abs(from - to)

        when {
            dx > dy -> when {
                from.x < to.x -> RIGHT
                else -> LEFT
            }
            else -> when {
                from.y < to.y -> DOWN
                else -> UP
            }
        }.let { game.direction = it }
    }

    private fun update(gameState: GameState) {
        state = when (gameState) {
            is Waiting -> State(
                introVisible = true,
                gameVisible = false,
                overVisible = false,
                field = null,
                snake = null,
                apples = null,
                score = null
            )
            is Running -> State(
                introVisible = false,
                gameVisible = true,
                overVisible = false,
                field = gameState.field,
                snake = gameState.snake.cells,
                apples = gameState.apples.cells,
                score = gameState.score
            )
            is Over -> State(
                introVisible = false,
                gameVisible = false,
                overVisible = true,
                field = null,
                snake = null,
                apples = null,
                score = gameState.score
            )
        }
    }
}