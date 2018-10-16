package cz.jhutarek.snake.game.model

data class Cell(
    val x: Int,
    val y: Int
) {
    fun move(direction: Direction) = Cell(x + direction.dx, y + direction.dy)

    operator fun plus(cells: List<Cell>): List<Cell> = listOf(this) + cells
}