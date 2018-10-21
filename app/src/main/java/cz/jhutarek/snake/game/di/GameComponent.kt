package cz.jhutarek.snake.game.di

import android.content.Context
import cz.jhutarek.snake.game.system.GameActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [GameModule::class])
interface GameComponent {

    fun inject(gameActivity: GameActivity)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun applicationContext(context: Context): Builder

        fun build(): GameComponent
    }
}