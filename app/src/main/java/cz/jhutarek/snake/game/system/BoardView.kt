package cz.jhutarek.snake.game.system

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.Style.FILL
import android.graphics.Paint.Style.STROKE
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import cz.jhutarek.snake.R
import cz.jhutarek.snake.game.model.Board
import kotlin.math.min

class BoardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val backgroundColor = getColor(R.color.board_background)
    private val gridColor = getColor(R.color.board_grid)
    private val snakeColor = getColor(R.color.board_snake)
    private val appleColor = getColor(R.color.board_apple)

    private val gridPaint = Paint().apply {
        color = gridColor
        strokeWidth = 2f
        style = STROKE
    }
    private val snakePaint = Paint().apply {
        color = snakeColor
        style = FILL
    }
    private val applePaint = Paint().apply {
        color = appleColor
        style = FILL
        isAntiAlias = true
    }

    private var gridSize: Float = 0f

    var board: Board? = null
        set(value) {
            field = value
            updateGridSize()
            invalidate()
        }

    private fun updateGridSize() {
        gridSize = board?.let {
            min(width.toFloat() / it.dimensions.width, height.toFloat() / it.dimensions.height)
        } ?: 0f
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        updateGridSize()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(backgroundColor)

        board?.let {
            drawHorizontalGridLines(canvas, it)
            drawVerticalGridLines(canvas, it)
            drawApples(canvas, it)
            drawSnake(canvas, it)
        }
    }

    private fun drawHorizontalGridLines(canvas: Canvas, board: Board) {
        for (i in 0..board.dimensions.height) {
            canvas.drawLine(
                0f, i * gridSize,
                board.dimensions.width * gridSize, i * gridSize,
                gridPaint
            )
        }
    }

    private fun drawVerticalGridLines(canvas: Canvas, board: Board) {
        for (i in 0..board.dimensions.width) {
            canvas.drawLine(
                i * gridSize, 0f,
                i * gridSize, board.dimensions.height * gridSize,
                gridPaint
            )
        }
    }

    private fun drawApples(canvas: Canvas, board: Board) {
        board.apples.forEach {
            canvas.drawCircle(
                (it.x + 0.5f) * gridSize, (it.y + 0.5f) * gridSize,
                gridSize / 2f,
                applePaint
            )
        }
    }

    private fun drawSnake(canvas: Canvas, board: Board) {
        board.snake.forEach {
            canvas.drawRect(
                it.x * gridSize, it.y * gridSize,
                (it.x + 1) * gridSize, (it.y + 1) * gridSize,
                snakePaint
            )
        }
    }

    private fun getColor(@ColorRes resId: Int) = ContextCompat.getColor(context, resId)
}