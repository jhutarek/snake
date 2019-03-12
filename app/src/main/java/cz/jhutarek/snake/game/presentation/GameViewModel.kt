package cz.jhutarek.snake.game.presentation

import android.graphics.Point
import javax.inject.Inject
import javax.inject.Singleton

// TODO uncomment
// import cz.jhutarek.snake.game.model.State as GameState

typealias GameViewModelListener = (GameViewModel.State) -> Unit

@Singleton
class GameViewModel @Inject constructor(
// TODO uncomment
//    private val game: Game
) {
    init {
// TODO uncomment
//        game.listener = ::update
    }

    data class State(
        val introVisible: Boolean,
        val gameVisible: Boolean,
        val overVisible: Boolean,
// TODO uncomment
//        val field: Dimensions?,
//        val snake: List<Cell>?,
//        val apples: Set<Cell>?,
        val score: String?
    )

    private var state = State(
        introVisible = true,
        gameVisible = false,
        overVisible = false,
// TODO uncomment
//        field = null,
//        snake = null,
//        apples = null,
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
// TODO uncomment
//        game.reset()
    }

    fun start() {
// TODO uncomment
//        game.start()
    }

    fun swipe(from: Point, to: Point) {
// TODO uncomment
//        val (dx, dy) = abs(from - to)
//
//        when {
//            dx > dy -> when {
//                from.x < to.x -> RIGHT
//                else -> LEFT
//            }
//            else -> when {
//                from.y < to.y -> DOWN
//                else -> UP
//            }
//        }.let { game.direction = it }
    }

// TODO uncomment
//    private fun update(gameState: GameState) {
//        state = when (gameState) {
//            is Waiting -> State(
//                introVisible = true,
//                gameVisible = false,
//                overVisible = false,
//                field = null,
//                snake = null,
//                apples = null,
//                score = null
//            )
//            is Running -> State(
//                introVisible = false,
//                gameVisible = true,
//                overVisible = false,
//                field = gameState.field,
//                snake = gameState.snake.cells,
//                apples = gameState.apples.cells,
//                score = gameState.score.toString()
//            )
//            is Over -> State(
//                introVisible = false,
//                gameVisible = false,
//                overVisible = true,
//                field = null,
//                snake = null,
//                apples = null,
//                score = gameState.score.toString()
//            )
//        }
//    }
}