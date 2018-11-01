package cz.jhutarek.snake.game.domain

import cz.jhutarek.snake.game.model.Cell
import cz.jhutarek.snake.game.model.Dimensions
import cz.jhutarek.snake.game.model.Direction
import cz.jhutarek.snake.game.model.State
import cz.jhutarek.snake.game.model.State.*
import javax.inject.Singleton

@Singleton
class StateUpdater(val initialRunningState: State.Running) {

    fun update(previous: State, direction: Direction) = when (previous) {
        is Waiting -> initialRunningState
        is Running -> previous.snake
            .turn(direction)
            .move()
            .eat(previous.apples.grow())
            .let {
                val (newSnake, newApples) = it

                if (newSnake.head in newSnake.tail || newSnake.head !in previous.field) State.Over(previous.score)
                else previous.copy(snake = newSnake, apples = newApples)
            }
        is Over -> previous
    }

    private operator fun Dimensions.contains(cell: Cell) = cell.x in 0 until width && cell.y in 0 until height
}