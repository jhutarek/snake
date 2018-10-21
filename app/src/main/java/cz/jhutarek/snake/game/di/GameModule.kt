package cz.jhutarek.snake.game.di

import cz.jhutarek.snake.game.device.TickerImpl
import cz.jhutarek.snake.game.domain.Game
import cz.jhutarek.snake.game.domain.StateUpdater
import cz.jhutarek.snake.game.domain.Ticker
import cz.jhutarek.snake.game.model.*
import cz.jhutarek.snake.game.model.Direction.LEFT
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class GameModule {

    companion object {
        private val FIELD_DIMENSIONS = Dimensions(20, 20)
        private val INITIAL_SNAKE = Snake((10..14).map { Cell(it, 10) }, LEFT)
        private const val INTERVAL = 150L
    }

    @Provides
    @Singleton
    fun stateUpdater(): StateUpdater = StateUpdater(
        State.Running(
            field = FIELD_DIMENSIONS,
            snake = INITIAL_SNAKE,
            apples = Apples(field = FIELD_DIMENSIONS)
        )
    )


    @Provides
    @Singleton
    fun ticker(tickerImpl: TickerImpl): Ticker = tickerImpl

    @Provides
    @Singleton
    fun game(stateUpdater: StateUpdater, ticker: Ticker): Game = Game(stateUpdater, ticker, INTERVAL)
}