package cz.jhutarek.snake.game.system

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cz.jhutarek.snake.R
import cz.jhutarek.snake.game.presentation.GameViewModel
import javax.inject.Inject

class GameActivity : AppCompatActivity() {

    @Inject internal lateinit var viewModel: GameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GameApplication.getInjector(this).inject(this)

        setContentView(R.layout.game__game_activity)
    }

    /* private class GameGestureListener : GestureDetector.SimpleOnGestureListener() {
         override fun onDown(e: MotionEvent) = true

         override fun onFling(
             e1: MotionEvent,
             e2: MotionEvent,
             velocityX: Float,
             velocityY: Float
         ): Boolean {
             val dx = abs(e1.x - e2.x)
             val dy = abs(e1.y - e2.y)

             when {
                 dx > dy -> when {
                     e1.x < e2.x -> listener?.onSwipe(RIGHT)
                     else -> listener?.onSwipe(LEFT)
                 }
                 else -> when {
                     e1.y < e2.y -> listener?.onSwipe(DOWN)
                     else -> listener?.onSwipe(UP)
                 }
             }

             return true
         }
     }

     private val gameGestureDetector by lazy {
         GestureDetectorCompat(this, GameGestureListener().apply {
             listener = object : GameGestureListener.Listener {
                 override fun onSwipe(direction: GameGestureListener.Direction) {
                     game.direction = when (direction) {
                         UP -> Direction.UP
                         DOWN -> Direction.DOWN
                         LEFT -> Direction.LEFT
                         RIGHT -> Direction.RIGHT
                     }
                 }
             }
         })
     }

     override fun onTouchEvent(event: MotionEvent): Boolean {
         return gameGestureDetector.onTouchEvent(event).let {
             if (!it) super.onTouchEvent(event) else it
         }
     }*/
}
