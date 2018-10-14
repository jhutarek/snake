package cz.jhutarek.snake.game.model

data class Board(
        val dimensions: Dimensions,
        val snake: Set<Cell>,
        val apples: Set<Cell>
)