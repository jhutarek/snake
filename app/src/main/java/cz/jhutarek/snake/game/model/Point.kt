package cz.jhutarek.snake.game.model

import kotlin.math.abs as absFloat

data class Point(val x: Float, val y: Float) {

    operator fun minus(other: Point) = Point(this.x - other.x, this.y - other.y)
}

fun abs(point: Point) = Point(absFloat(point.x), absFloat(point.y))