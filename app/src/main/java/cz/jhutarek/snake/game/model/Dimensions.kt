package cz.jhutarek.snake.game.model

data class Dimensions(
    val width: Int,
    val height: Int
) {
    init {
        require(width > 0) { "Width must be positive" }
        require(height > 0) { "Height must be positive" }
    }
}