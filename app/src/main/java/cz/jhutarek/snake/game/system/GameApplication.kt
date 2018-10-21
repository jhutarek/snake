package cz.jhutarek.snake.game.system

import android.app.Application
import android.content.Context
import cz.jhutarek.snake.game.di.DaggerGameComponent
import cz.jhutarek.snake.game.di.GameComponent

class GameApplication : Application() {

    companion object {
        fun getInjector(context: Context) = (context.applicationContext as? GameApplication)
            ?.component
            ?: throw IllegalStateException("Cannot obtain injector")
    }

    private lateinit var component: GameComponent

    override fun onCreate() {
        super.onCreate()

        component = DaggerGameComponent.create()
    }
}