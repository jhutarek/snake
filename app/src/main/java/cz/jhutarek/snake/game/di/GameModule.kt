package cz.jhutarek.snake.game.di

import cz.jhutarek.snake.game.device.TickerImpl
import cz.jhutarek.snake.game.domain.StateUpdater
import cz.jhutarek.snake.game.domain.Ticker
import cz.jhutarek.snake.game.model.*
import cz.jhutarek.snake.game.model.Direction.LEFT
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class GameModule {

    @Provides
    @Singleton
    fun stateUpdater(): StateUpdater {
        val field = Dimensions(20, 20)

        return StateUpdater(
            State.Running(
                field = field,
                snake = Snake((10..14).map { Cell(it, 10) }, LEFT),
                apples = Apples(field = field)
            )
        )
    }

    @Provides
    @Singleton
    fun ticker(tickerImpl: TickerImpl): Ticker = tickerImpl
}