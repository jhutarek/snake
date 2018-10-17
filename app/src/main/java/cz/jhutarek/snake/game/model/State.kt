package cz.jhutarek.snake.game.model

sealed class State {
    object Waiting : State() {
        override fun toString() = "Waiting"
    }

    data class Running(
        val dimensions: Dimensions,
        val snake: Snake,
        val apples: Apples,
        val score: Int
    ) : State()

    data class Over(
        val score: Int
    ) : State()
}