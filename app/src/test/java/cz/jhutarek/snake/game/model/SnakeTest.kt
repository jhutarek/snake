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
    private val apples = Apples(field = Dimensions(20, 20))
    private val anySnake = Snake(snakeCells, anyDirection)

    @Test
    fun `should require non empty cells`() {
        assertThatThrownBy { Snake(listOf(), anyDirection) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `should return first cell as head`() {
        assertThat(anySnake.head)
            .isEqualTo(headCell)
    }

    @Test
    fun `should return all cells except the first as tail`() {
        assertThat(anySnake.tail)
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

    @ParameterizedTest
    @MethodSource("turnData")
    fun `should turn if new direction is perpendicular`(expectedSnakeDirection: Direction, originalSnakeDirection: Direction, direction: Direction) {
        assertThat(Snake(snakeCells, originalSnakeDirection).turn(direction))
            .isEqualTo(Snake(snakeCells, expectedSnakeDirection))
    }

    private fun turnData() = Stream.of(
        arguments(LEFT, LEFT, RIGHT),
        arguments(UP, LEFT, UP),
        arguments(DOWN, LEFT, DOWN),
        arguments(LEFT, LEFT, LEFT)
    )

    @Test
    fun `should return original snake and apples if no apple was eaten`() {
        assertThat(anySnake.eat(apples))
            .isEqualTo(Pair(anySnake, apples))
    }

    @Test
    fun `should return new snake with apples eaten and future growth and apples without eaten apple if apple was eaten`() {
        val apples = apples.copy(cells = setOf(headCell, Cell(0, 0)))

        assertThat(anySnake.eat(apples))
            .isEqualTo(Pair(anySnake.copy(futureGrowth = 1, applesEaten = 1), apples.copy(cells = setOf(Cell(0, 0)))))
    }

    @Test
    fun `should grow tail on next move if future growth is greater than zero`() {
        assertThat(anySnake.copy(futureGrowth = 1).move())
            .isEqualTo(anySnake.copy(cells = listOf(Cell(2, 3), Cell(3, 3), Cell(4, 3), Cell(5, 3)), futureGrowth = 0))
    }
}