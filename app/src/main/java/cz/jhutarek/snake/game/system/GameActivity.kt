package cz.jhutarek.snake.game.system

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cz.jhutarek.snake.R

class GameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.game__game_activity)
    }
}
