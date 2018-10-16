package cz.jhutarek.snake.game.model

data class Snake(
    val cells: List<Cell>,
    val direction: Direction
) {
    init {
        require(cells.isNotEmpty()) { "Cells must not be empty" }
        require(head.move(direction) != tail.firstOrNull()) { "Direction must not reverse the snake" }
    }

    val head get() = cells.first()
    val tail get() = cells.drop(1)

    fun move(): Snake {
        val newHead = head.move(direction)
        val newTail = cells.dropLast(1)

        return copy(cells = newHead + newTail)
    }

    fun turn(direction: Direction) =
        if (this.direction.isPerpendicular(direction)) copy(direction = direction)
        else this

    fun eat(apples: Apples) =
        if (head in apples.cells) Pair(this, apples.copy(cells = apples.cells - head))
        else Pair(this, apples)
}