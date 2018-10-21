package cz.jhutarek.snake.game.device

import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import cz.jhutarek.snake.game.domain.Vibrator
import cz.jhutarek.snake.game.domain.Vibrator.Pattern.APPLE_EATEN
import cz.jhutarek.snake.game.domain.Vibrator.Pattern.SNAKE_DIED
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VibratorImpl @Inject constructor(
    private val context: Context
) : Vibrator {

    override fun vibrate(pattern: Vibrator.Pattern) {
        (context.getSystemService(VIBRATOR_SERVICE) as? android.os.Vibrator)?.vibrate(
            when (pattern) {
                APPLE_EATEN -> 400L
                SNAKE_DIED -> 800L
            }
        )
    }
}