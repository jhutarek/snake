package cz.jhutarek.snake.game.model

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class DimensionsTest {

    @ParameterizedTest
    @MethodSource("positiveDimensionsData")
    fun `should require both dimensions to be positive`(width: Int, height: Int) {
        assertThatThrownBy { Dimensions(width, height) }.isInstanceOf(IllegalArgumentException::class.java)
    }

    private fun positiveDimensionsData() = Stream.of(
        arguments(-10, 1),
        arguments(-1, 1),
        arguments(0, 1),
        arguments(1, -10),
        arguments(1, -1),
        arguments(1, 0),
        arguments(0, 0)
    )
}