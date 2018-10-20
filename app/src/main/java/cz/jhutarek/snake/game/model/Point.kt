package cz.jhutarek.snake.game.model

import kotlin.math.abs as absInt

data class Point(val x: Int, val y: Int) {

    operator fun minus(other: Point) = Point(this.x - other.x, this.y - other.y)
}

fun abs(point: Point) = Point(absInt(point.x), absInt(point.y))