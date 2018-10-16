package cz.jhutarek.snake.game.model

import cz.jhutarek.snake.game.model.Direction.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class DirectionTest {

    @ParameterizedTest
    @MethodSource("dxDyData")
    fun `direction should have correct dx and dy`(expectedDx: Int, expectedDy: Int, direction: Direction) {
        assertThat(direction.dx).isEqualTo(expectedDx)
        assertThat(direction.dy).isEqualTo(expectedDy)
    }

    private fun dxDyData() = Stream.of(
        arguments(0, -1, UP),
        arguments(0, 1, DOWN),
        arguments(-1, 0, LEFT),
        arguments(1, 0, RIGHT)
    )

    @ParameterizedTest
    @MethodSource("oppositeData")
    fun `direction should return correct opposite result`(expectedOpposite: Boolean, direction1: Direction, direction2: Direction) {
        assertThat(direction1.isOpposite(direction2)).isEqualTo(expectedOpposite)
    }

    private fun oppositeData() = Stream.of(
        arguments(true, UP, DOWN),
        arguments(true, DOWN, UP),
        arguments(true, LEFT, RIGHT),
        arguments(true, RIGHT, LEFT),
        arguments(false, UP, UP),
        arguments(false, DOWN, RIGHT)
    )
}