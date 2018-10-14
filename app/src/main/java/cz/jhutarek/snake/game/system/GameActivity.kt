package cz.jhutarek.snake.game.system

import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import cz.jhutarek.snake.R
import cz.jhutarek.snake.game.model.Board
import cz.jhutarek.snake.game.model.Cell
import cz.jhutarek.snake.game.model.Dimensions
import cz.jhutarek.snake.game.system.GameActivity.GameGestureListener.Direction.*
import kotlinx.android.synthetic.main.game__game_activity.*
import java.lang.Math.abs

class GameActivity : AppCompatActivity() {

    private class GameGestureListener : GestureDetector.SimpleOnGestureListener() {
        enum class Direction { UP, DOWN, LEFT, RIGHT }

        interface Listener {
            fun onSwipe(direction: Direction)
        }

        var listener: Listener? = null

        override fun onDown(e: MotionEvent) = true

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
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
                    Log.d("XXXXXXXXXX", "Swipe: $direction")
                }
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.game__game_activity)

        board.board = Board(
                Dimensions(20, 20),
                listOf(Cell(3, 1), Cell(4, 1), Cell(5, 1), Cell(5, 2), Cell(5, 3), Cell(5, 4), Cell(5, 5)),
                listOf(Cell(0, 1), Cell(1, 3), Cell(2, 6), Cell(7, 3))
        )
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gameGestureDetector.onTouchEvent(event).let {
            if (!it) super.onTouchEvent(event) else it
        }
    }
}
