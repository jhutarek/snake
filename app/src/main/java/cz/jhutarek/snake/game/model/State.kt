package cz.jhutarek.snake.game.model

sealed class State {
    object Waiting : State() {
        override fun toString() = "Waiting"
    }

    data class Running(
        val field: Dimensions,
        val snake: Snake,
        val apples: Apples
    ) : State() {
        companion object {
            private const val SCORE_MULTIPLIER = 100
        }

        val score = snake.applesEaten * SCORE_MULTIPLIER
    }

    data class Over(
        val score: Int
    ) : State()
}