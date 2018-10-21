package cz.jhutarek.snake.game.di

import cz.jhutarek.snake.game.system.GameActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [GameModule::class])
interface GameComponent {
    fun inject(gameActivity: GameActivity)
}