package cz.jhutarek.snake.game.testinfrastructure

import io.kotlintest.specs.AbstractStringSpec
import io.kotlintest.specs.StringSpec

abstract class CustomStringSpec(body: AbstractStringSpec.() -> Unit = {}) : StringSpec(body) {
    final override fun isInstancePerTest() = true
}