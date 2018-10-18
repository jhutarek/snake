package cz.jhutarek.snake.gameold.system

import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import cz.jhutarek.snake.R
import cz.jhutarek.snake.game.device.TickerImpl
import cz.jhutarek.snake.game.domain.Game
import cz.jhutarek.snake.game.domain.StateUpdater
import cz.jhutarek.snake.game.model.*
import cz.jhutarek.snake.gameold.system.GameActivity.GameGestureListener.Direction.*
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

    private val game = Game(
        StateUpdater(
            State.Running(
                Dimensions(20, 20),
                Snake(cells = (10..14).map { Cell(it, 10) }, direction = Direction.LEFT),
                Apples(field = Dimensions(20, 20))
            )
        ),
        TickerImpl()
    ).apply {
        listener = {
            when (it) {
                is State.Running -> {
                    this@GameActivity.board.board = it
                    this@GameActivity.score.text = it.score.toString()
                }
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
