package cz.jhutarek.snake.gameold.model

data class Board(
    val dimensions: Dimensions,
    val snake: Set<Cell>,
    val apples: Set<Cell>
)