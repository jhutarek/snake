package cz.jhutarek.snake.game.model

import kotlin.random.Random

data class Apples(
        private val dimensions: Dimensions,
        val cells: Set<Cell> = emptySet(),
        private val growthSpeed: Double = 0.1,
        private val maxApples: Int = 5
) {
    fun grow() =
            if (Random.nextDouble() >= growthSpeed || cells.size >= maxApples) this
            else copy(
                    cells = cells + Cell(Random.nextInt(dimensions.width), Random.nextInt(dimensions.height))
            )
}