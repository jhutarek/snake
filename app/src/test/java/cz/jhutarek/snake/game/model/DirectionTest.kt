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
    @MethodSource("perpendicularData")
    fun `direction should return correct perpendicular result`(expectedPerpendicular: Boolean, direction1: Direction, direction2: Direction) {
        assertThat(direction1.isPerpendicular(direction2)).isEqualTo(expectedPerpendicular)
    }

    private fun perpendicularData() = Stream.of(
        arguments(false, UP, DOWN),
        arguments(false, DOWN, UP),
        arguments(false, LEFT, RIGHT),
        arguments(false, RIGHT, LEFT),
        arguments(false, UP, UP),
        arguments(false, RIGHT, RIGHT),
        arguments(false, LEFT, LEFT),
        arguments(false, DOWN, DOWN),
        arguments(true, UP, LEFT),
        arguments(true, LEFT, UP),
        arguments(true, LEFT, DOWN),
        arguments(true, DOWN, LEFT),
        arguments(true, DOWN, RIGHT),
        arguments(true, RIGHT, DOWN),
        arguments(true, RIGHT, UP),
        arguments(true, UP, RIGHT)
    )
}