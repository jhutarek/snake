package cz.jhutarek.snake.game.model

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class DimensionsTest {

    @ParameterizedTest
    @ArgumentsSource(PositiveDimensionsProvider::class)
    fun `should require both dimensions to be positive`(width: Int, height: Int) {
        assertThatThrownBy { Dimensions(width, height) }.isInstanceOf(IllegalArgumentException::class.java)
    }

    private class PositiveDimensionsProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext): Stream<Arguments> = Stream.of(
            arguments(-10, 1),
            arguments(-1, 1),
            arguments(0, 1),
            arguments(1, -10),
            arguments(1, -1),
            arguments(1, 0),
            arguments(0, 0)
        )
    }
}