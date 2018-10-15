package cz.jhutarek.snake.game.model

data class Snake(
    val cells: List<Cell>,
    private val direction: Direction,
    private val applesDigesting: Int = 0,
    val applesEaten: Int = 0
) {
    val head = cells.first()
    val tail = cells.drop(1)

    fun move(): Snake {
        val newHead = head.move(direction)
        val newTail = if (applesDigesting == 0) cells.dropLast(1) else cells

        return copy(
            cells = listOf(newHead) + newTail,
            applesDigesting = maxOf(0, applesDigesting - 1)
        )
    }

    fun turn(newDirection: Direction) =
        if (newDirection.isOpposite(direction)) this
        else copy(direction = newDirection)

    fun eat(apples: Apples) =
        if (!apples.cells.contains(head)) Pair(this, apples)
        else Pair(
            this.copy(applesDigesting = applesDigesting + 1, applesEaten = applesEaten + 1),
            apples.copy(cells = apples.cells - head)
        )
}