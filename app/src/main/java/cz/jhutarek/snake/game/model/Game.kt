package cz.jhutarek.snake.game.model

import cz.jhutarek.snake.game.model.Direction.LEFT
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class Game {

    companion object {
        private val DIMENSIONS = Dimensions(20, 20)
    }

    interface Listener {
        fun onUpdate(board: Board, score: Int, isOver: Boolean)
    }

    var listener: Listener? = null
    private var lastDirection: Direction = LEFT
    private var snake = Snake(
        listOf(Cell(10, 10), Cell(11, 10), Cell(12, 10), Cell(14, 10), Cell(15, 10)),
        lastDirection
    )
    private var apples = Apples(DIMENSIONS)
    private var tickerDisposable: Disposable? = null

    fun setDirection(direction: Direction) {
        lastDirection = direction
    }

    fun start() {
        tickerDisposable = Observable.interval(150, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.computation())
            .subscribe { update() }
    }

    private fun update() {
        if (!isOver()) {
            val (newSnake, newApples) = snake
                .turn(lastDirection)
                .move()
                .eat(apples.grow())

            snake = newSnake
            apples = newApples
        } else {
            tickerDisposable?.dispose()
        }

        listener?.onUpdate(
            Board(DIMENSIONS, snake.cells.toSet(), apples.cells),
            snake.applesEaten * 100,
            isOver()
        )
    }

    private fun isOver() =
        snake.tail.contains(snake.head) || snake.cells.any { it.x !in 0 until DIMENSIONS.width || it.y !in 0 until DIMENSIONS.height }
}