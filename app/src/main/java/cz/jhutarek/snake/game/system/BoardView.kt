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
import cz.jhutarek.snake.game.model.Cell
import cz.jhutarek.snake.game.model.Dimensions
import kotlin.math.min

class BoardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val gridSubDivisions = 3
    }

    data class State(
        val field: Dimensions,
        val snake: List<Cell>,
        val apples: Set<Cell>
    )

    private val backgroundColor = getColor(R.color.board_background)
    private val gridColor = getColor(R.color.board_grid)
    private val snakeColor = getColor(R.color.board_snake)
    private val appleColor = getColor(R.color.board_apple)

    private val gridPaint = Paint().apply {
        color = gridColor
        strokeWidth = 1f
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

    var state: State? = null
        set(value) {
            field = value
            updateGridSize()
            invalidate()
        }

    private fun updateGridSize() {
        gridSize = state?.let {
            min(width.toFloat() / it.field.width, height.toFloat() / it.field.height)
        } ?: 0f
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        updateGridSize()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(backgroundColor)

        state?.let {
            drawApples(canvas, it)
            drawSnake(canvas, it)
            drawHorizontalGridLines(canvas, it)
            drawVerticalGridLines(canvas, it)
        }
    }

    private fun drawHorizontalGridLines(canvas: Canvas, board: State) {
        for (i in 0..board.field.height * gridSubDivisions) {
            canvas.drawLine(
                0f, i * gridSize / gridSubDivisions,
                board.field.width * gridSize, i * gridSize / gridSubDivisions,
                gridPaint
            )
        }
    }

    private fun drawVerticalGridLines(canvas: Canvas, board: State) {
        for (i in 0..board.field.width * gridSubDivisions) {
            canvas.drawLine(
                i * gridSize / gridSubDivisions, 0f,
                i * gridSize / gridSubDivisions, board.field.height * gridSize,
                gridPaint
            )
        }
    }

    private fun drawApples(canvas: Canvas, board: State) {
        board.apples.forEach {
            canvas.drawCircle(
                (it.x + 0.5f) * gridSize, (it.y + 0.5f) * gridSize,
                gridSize / 2f,
                applePaint
            )
        }
    }

    private fun drawSnake(canvas: Canvas, board: State) {
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