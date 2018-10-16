package cz.jhutarek.snake.game.model

import cz.jhutarek.snake.game.model.Direction.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream
import java.util.stream.Stream.of

internal class CellTest {

    @ParameterizedTest
    @ArgumentsSource(CellMoveProvider::class)
    fun `cell should move in given direction`(expectedCell: Cell, originalCell: Cell, direction: Direction) {
        assertThat(originalCell + direction).isEqualTo(expectedCell)
    }

    private class CellMoveProvider : ArgumentsProvider {
        private val anyCell = Cell(5, 3)

        override fun provideArguments(context: ExtensionContext): Stream<Arguments> = of(
            arguments(Cell(4, 3), anyCell, LEFT),
            arguments(Cell(6, 3), anyCell, RIGHT),
            arguments(Cell(5, 2), anyCell, UP),
            arguments(Cell(5, 4), anyCell, DOWN)
        )
    }
}