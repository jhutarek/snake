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

internal class DirectionTest {

    @ParameterizedTest
    @ArgumentsSource(DxDyProvider::class)
    fun `direction should have correct dx and dy`(expectedDx: Int, expectedDy: Int, direction: Direction) {
        assertThat(direction.dx).isEqualTo(expectedDx)
        assertThat(direction.dy).isEqualTo(expectedDy)
    }

    private class DxDyProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext): Stream<Arguments> = Stream.of(
            arguments(0, -1, UP),
            arguments(0, 1, DOWN),
            arguments(-1, 0, LEFT),
            arguments(1, 0, RIGHT)
        )
    }

    @ParameterizedTest
    @ArgumentsSource(OppositeProvider::class)
    fun `direction should return correct opposite result`(expectedOpposite: Boolean, direction1: Direction, direction2: Direction) {
        assertThat(direction1.isOpposite(direction2)).isEqualTo(expectedOpposite)
    }

    private class OppositeProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext): Stream<Arguments> = Stream.of(
            arguments(true, UP, DOWN),
            arguments(true, DOWN, UP),
            arguments(true, LEFT, RIGHT),
            arguments(true, RIGHT, LEFT),
            arguments(false, UP, UP),
            arguments(false, DOWN, RIGHT)
        )
    }
}