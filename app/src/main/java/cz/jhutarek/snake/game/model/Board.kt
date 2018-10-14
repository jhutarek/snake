package cz.jhutarek.snake.game.model

data class Board(
        val dimensions: Dimensions,
        val snake: List<Cell>,
        val apples: List<Cell>
)