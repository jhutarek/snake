package cz.jhutarek.snake.game.system

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cz.jhutarek.snake.R
import cz.jhutarek.snake.game.model.Board
import cz.jhutarek.snake.game.model.Cell
import cz.jhutarek.snake.game.model.Dimensions
import kotlinx.android.synthetic.main.game__game_activity.*

class GameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.game__game_activity)

        board.board = Board(
                Dimensions(20, 20),
                listOf(Cell(3, 1), Cell(4, 1), Cell(5, 1), Cell(5, 2), Cell(5, 3), Cell(5, 4), Cell(5, 5)),
                listOf(Cell(0, 1), Cell(1, 3), Cell(2, 6), Cell(7, 3))
        )
    }
}
