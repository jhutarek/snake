package cz.jhutarek.snake.game.system

import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import cz.jhutarek.snake.R
import cz.jhutarek.snake.game.model.Board
import cz.jhutarek.snake.game.model.Direction
import cz.jhutarek.snake.game.model.Game
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
                    game.setDirection(
                            when (direction) {
                                UP -> Direction.UP
                                DOWN -> Direction.DOWN
                                LEFT -> Direction.LEFT
                                RIGHT -> Direction.RIGHT
                            }
                    )
                }
            }
        })
    }

    private val game = Game().apply {
        listener = object : Game.Listener {
            override fun onUpdate(board: Board, score: Int, isOver: Boolean) {
                this@GameActivity.board.board = board
                this@GameActivity.score.text = score.toString()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.game__game_activity)

        game.start()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gameGestureDetector.onTouchEvent(event).let {
            if (!it) super.onTouchEvent(event) else it
        }
    }
}
