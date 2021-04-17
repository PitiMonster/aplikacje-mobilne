package com.example.tictactoe

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.View
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow

import com.example.tictactoe.databinding.ActivityGameBinding


class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    private var size: Int = 0;
    private var board = arrayOf<CharArray>()
    private var movesCnt = 0;

    var currPlayer: Char = 'O';

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)


        setUpBoard(view)


    }

    private fun finishGame() {
        val returnIntent = Intent()
        println(movesCnt)
        val resp: Int = if (movesCnt > 0) {
            if (currPlayer == 'X') 1
            else 0
        } else 2
        println(resp)
        returnIntent.putExtra("winner", resp)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    private fun setUpBoard(view: View) {


        val intent = intent
        size = intent.getIntExtra("size", 3)
        movesCnt = size * size

        //board init
        for (i in 0 until size) {
            board += CharArray(size)
        }

        for (i in 0 until size) {
            val tableRow = TableRow(this)
            val params = TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT,
                1f
            )
            tableRow.layoutParams = params

            for (j in 0 until size) {
                val button = Button(this)
                val params2 = TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT,
                    1f
                )
                button.layoutParams = params2
//                button.setBackgroundColor(Color.BLUE)
                tableRow.addView(button)
                button.setOnClickListener { view -> drawShape(view, i, j) }
            }

            binding.mainLayout.addView(tableRow);

        }
    }

    private fun drawShape(view: View, i: Int, j: Int) {
        with(view as Button) {
            if (view.text == "") {
                view.text = currPlayer.toString()
                board[i][j] = currPlayer
                checkWinner(i, j)
                currPlayer = if (currPlayer == 'O') {
                    'X'
                } else {
                    'O'
                }
                movesCnt--
                if (movesCnt == 0) {
                    finishGame()
                }
            }
        }
    }

    private fun checkWinner(i: Int, j: Int) {
        for (k in 0 until size) {
            if (board[i][k] == currPlayer) {
                if (k == size - 1) {
                    finishGame();
                }
            } else {
                break
            }
        }

        for (k in 0 until size) {

            if (board[k][j] == currPlayer) {
                if (k == size - 1) {
                    finishGame()
                }
            } else {
                break
            }
        }

        if (i == j) {

            for (k in 0 until size) {
                if (board[k][k] == currPlayer) {
                    if (k == size - 1) {
                        finishGame()
                    }
                } else {
                    break
                }
            }
        }

        if (i + j == size - 1) {

            for (k in 0 until size) {
                if (board[size - k - 1][k] == currPlayer) {
                    if (k == size - 1) {
                        finishGame()
                    }
                } else {
                    break
                }
            }
        }
    }


}