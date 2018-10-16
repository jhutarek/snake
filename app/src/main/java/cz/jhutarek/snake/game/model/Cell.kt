package cz.jhutarek.snake.game.model

data class Cell(
    val x: Int,
    val y: Int
) {
    operator fun plus(direction: Direction) = Cell(x + direction.dx, y + direction.dy)
}