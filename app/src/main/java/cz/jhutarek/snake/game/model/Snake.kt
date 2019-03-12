package cz.jhutarek.snake.game.model

data class Snake(
    val cells: List<Cell>,
    val direction: Direction,
    val applesEaten: Int = 0,
    val futureGrowth: Int = 0
) {
    companion object {
        private const val APPLE_FUTURE_GROWTH = 1
    }

    data class EatingResult(val snake: Snake, val apples: Apples)

    init {
        require(cells.isNotEmpty()) { "Cells must not be empty" }
        require(head.move(direction) != tail.firstOrNull()) { "Direction must not reverse the snake" }
        require(applesEaten >= 0) { "applesEaten must not be negative" }
        require(futureGrowth >= 0) { "futureGrowth must not be negative" }
    }

    val head get() = cells.first()
    val tail get() = cells.drop(1)

    fun move(): Snake {
        val newHead = head.move(direction)
        val newTail = if (futureGrowth > 0) cells else cells.dropLast(1)

        return copy(
            cells = newHead + newTail,
            futureGrowth = (futureGrowth - 1).coerceAtLeast(0)
        )
    }

    fun turn(direction: Direction) =
        if (this.direction isPerpendicularTo direction) copy(direction = direction)
        else this

    fun eat(apples: Apples) =
        if (head in apples.cells) EatingResult(
            snake = this.copy(
                futureGrowth = futureGrowth + APPLE_FUTURE_GROWTH,
                applesEaten = applesEaten + 1
            ),
            apples = apples.copy(
                cells = apples.cells - head
            )
        )
        else EatingResult(snake = this, apples = apples)
}