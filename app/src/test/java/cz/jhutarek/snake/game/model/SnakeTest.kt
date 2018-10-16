package cz.jhutarek.snake.game.model

import cz.jhutarek.snake.game.model.Direction.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class SnakeTest {

    private val headCell = Cell(3, 3)
    private val tailCells = listOf(Cell(4, 3), Cell(5, 3))
    private val snakeCells = headCell + tailCells
    private val anyDirection = LEFT

    @Test
    fun `should require non empty cells`() {
        assertThatThrownBy { Snake(listOf(), anyDirection) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `should return first cell as head`() {
        assertThat(Snake(snakeCells, anyDirection).head)
            .isEqualTo(headCell)
    }

    @Test
    fun `should return all cells except the first as tail`() {
        assertThat(Snake(snakeCells, anyDirection).tail)
            .containsExactlyElementsOf(tailCells)
    }

    @Test
    fun `should require direction to not reverse the snake`() {
        assertThatThrownBy { Snake(snakeCells, RIGHT) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @ParameterizedTest
    @MethodSource("moveData")
    fun `should move by one cell in given direction and keep the direction`(expectedSnake: Snake, direction: Direction) {
        assertThat(Snake(snakeCells, direction).move())
            .isEqualTo(expectedSnake)
    }

    private fun moveData() = Stream.of(
        arguments(Snake(listOf(Cell(2, 3), Cell(3, 3), Cell(4, 3)), LEFT), LEFT),
        arguments(Snake(listOf(Cell(3, 2), Cell(3, 3), Cell(4, 3)), UP), UP),
        arguments(Snake(listOf(Cell(3, 4), Cell(3, 3), Cell(4, 3)), DOWN), DOWN)
    )
}