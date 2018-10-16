package cz.jhutarek.snake.game.model

import cz.jhutarek.snake.game.model.Direction.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class CellTest {

    @ParameterizedTest
    @MethodSource("cellMoveData")
    fun `should move in given direction`(expectedCell: Cell, originalCell: Cell, direction: Direction) {
        assertThat(originalCell.move(direction))
            .isEqualTo(expectedCell)
    }

    private fun cellMoveData(): Stream<Arguments> {
        val anyCell = Cell(5, 3)

        return Stream.of(
            arguments(Cell(4, 3), anyCell, LEFT),
            arguments(Cell(6, 3), anyCell, RIGHT),
            arguments(Cell(5, 2), anyCell, UP),
            arguments(Cell(5, 4), anyCell, DOWN)
        )
    }

    @Test
    fun `should create list with other cells`() {
        assertThat(Cell(0, 0) + listOf(Cell(1, 1), Cell(1, 2)))
            .containsExactly(Cell(0, 0), Cell(1, 1), Cell(1, 2))
    }
}