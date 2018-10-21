package cz.jhutarek.snake.game.domain

interface Vibrator {

    enum class Pattern { APPLE_EATEN, SNAKE_DIED }

    fun vibrate(pattern: Pattern)
}