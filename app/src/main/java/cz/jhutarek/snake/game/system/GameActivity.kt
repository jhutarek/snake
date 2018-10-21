package cz.jhutarek.snake.game.system

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import cz.jhutarek.snake.R
import cz.jhutarek.snake.game.presentation.GameViewModel
import cz.jhutarek.snake.gameold.system.BoardView
import kotlinx.android.synthetic.main.game__game_activity.*
import kotlinx.android.synthetic.main.game__game_include.*
import kotlinx.android.synthetic.main.game__intro_include.*
import kotlinx.android.synthetic.main.game__over_include.*
import javax.inject.Inject

class GameActivity : AppCompatActivity() {

    @Inject internal lateinit var viewModel: GameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GameApplication.getInjector(this).inject(this)

        setContentView(R.layout.game__game_activity)

        start.setOnClickListener { viewModel.start() }
        reset.setOnClickListener { viewModel.reset() }
    }

    override fun onStart() {
        super.onStart()

        viewModel.listener = {
            intro.visible = it.introVisible
            game.visible = it.gameVisible
            over.visible = it.overVisible
            board.board =
                    if (it.field != null && it.snake != null && it.apples != null) BoardView.State(it.field, it.snake, it.apples)
                    else null
            gameScore.text = it.score
            overScore.text = it.score
        }
    }

    override fun onStop() {
        super.onStop()

        viewModel.listener = null
    }

    private var View.visible
        get() = this.visibility == VISIBLE
        set(value) {
            this.visibility = if (value) VISIBLE else GONE
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
