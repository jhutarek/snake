package cz.jhutarek.snake.game.presentation

import cz.jhutarek.snake.game.domain.Game
import cz.jhutarek.snake.game.model.Cell
import cz.jhutarek.snake.game.model.Dimensions
import cz.jhutarek.snake.game.model.Direction.*
import cz.jhutarek.snake.game.model.Point
import cz.jhutarek.snake.game.model.abs

typealias GameViewModelListener = (GameViewModel.State) -> Unit

class GameViewModel(
    private val game: Game
) {

    data class State(
        val introVisible: Boolean,
        val gameVisible: Boolean,
        val overVisible: Boolean,
        val field: Dimensions?,
        val snake: List<Cell>?,
        val apples: List<Cell>?,
        val score: Int?
    )

    var listener: GameViewModelListener? = null

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
}