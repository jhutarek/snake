package cz.jhutarek.snake.game.system

import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import cz.jhutarek.snake.R
import cz.jhutarek.snake.game.model.Point
import cz.jhutarek.snake.game.presentation.GameViewModel
import cz.jhutarek.snake.gameold.system.BoardView
import kotlinx.android.synthetic.main.game__game_activity.*
import kotlinx.android.synthetic.main.game__game_include.*
import kotlinx.android.synthetic.main.game__intro_include.*
import kotlinx.android.synthetic.main.game__over_include.*
import javax.inject.Inject

class GameActivity : AppCompatActivity() {

    @Inject internal lateinit var viewModel: GameViewModel

    private val gestureDetector by lazy {
        GestureDetectorCompat(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent) = true

            override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                viewModel.swipe(e1.toPoint(), e2.toPoint())
                return true
            }
        })
    }

    private var isGameRunning = false

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
            isGameRunning = it.gameVisible
        }
    }

    override fun onStop() {
        super.onStop()

        viewModel.listener = null
    }

    override fun onTouchEvent(event: MotionEvent) = if (isGameRunning) gestureDetector.onTouchEvent(event) else false

    private fun MotionEvent.toPoint() = Point(x, y)

    private var View.visible
        get() = this.visibility == VISIBLE
        set(value) {
            this.visibility = if (value) VISIBLE else GONE
        }
}
