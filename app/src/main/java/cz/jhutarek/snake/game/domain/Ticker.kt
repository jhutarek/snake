package cz.jhutarek.snake.game.domain

typealias TickerListener = () -> Unit

interface Ticker {

    var listener: TickerListener?

    fun start(intervalMillis: Long)

    fun stop()
}