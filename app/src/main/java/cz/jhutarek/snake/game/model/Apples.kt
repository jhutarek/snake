package cz.jhutarek.snake.game.model

import kotlin.random.Random

data class Apples(
    val field: Dimensions,
    val cells: Set<Cell> = emptySet(),
    val maxApples: Int = 3,
    val growthProbability: Double = 0.1,
    val randomGenerator: Random = Random
) {
    init {
        require(maxApples > 0) { "maxApples must be positive" }
        require(growthProbability >= 0.0 && growthProbability < 1.0) { "growthProbability must be in range [0, 1)" }
    }

    fun grow() =
        if (mustGrow) copy(cells = cells + field.getRandomCell())
        else this

    private val mustGrow get() = cells.size < maxApples && randomGenerator.nextDouble() < growthProbability

    private fun Dimensions.getRandomCell() = Cell(randomGenerator.nextInt(width), randomGenerator.nextInt(height))
}