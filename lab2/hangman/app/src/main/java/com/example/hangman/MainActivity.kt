package com.example.hangman

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.hangman.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    val MAX_ERRORS = 7
    var wordsArray = arrayOf<String>()
    private var letters = CharArray(0)
    var errorsAmount = 0
    var wordToFind = ""
    var isFinished = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        wordsArray += resources.getStringArray(R.array.words)
        startGame()
    }

    private fun getRandomWord(): String {
        return wordsArray.random()
    }

    private fun updateLetters(c: Char) {
        if (!letters.contains(c)) {
            if(wordToFind.contains(c)){
                for (i in wordToFind.indices) {
                    if(wordToFind[i] == c) {
                        letters[i] = c
                    }
                }
                binding.wordToGuess.text = String(letters)
            }
            else {
                errorsAmount++
                changeImage(errorsAmount)
                Toast.makeText(this, "Zła litera", Toast.LENGTH_SHORT).show()

            }
        } else {
            Toast.makeText(this, "Już zgadywałeś tą literę!", Toast.LENGTH_SHORT). show()
        }
    }

    private fun changeImage(errorsAmount: Int) {
        val imgName = "hangman${errorsAmount}"
        val identifier = resources.getIdentifier(imgName, "drawable", packageName)
        binding.imageViewHangman.setImageResource(identifier)
    }

    private fun hasWon(): Boolean {
        return String(letters) == wordToFind
    }

    private fun hasLost(): Boolean{
        return errorsAmount >= MAX_ERRORS
    }

    fun touchLetter(view: View) {
        val letter: Char = (view as Button).text[0].toLowerCase()

        if(!isFinished) updateLetters(letter)

        if (hasWon()) {
            Toast.makeText(this, "Gratulacje, wygrałeś!", Toast.LENGTH_SHORT).show()
            isFinished = true
        }

        if(hasLost()) {
            Toast.makeText(this, "Przegrałęś :(. Spróbuj ponownie!", Toast.LENGTH_SHORT).show()
            isFinished = true
        }
    }

    private fun startGame() {
        isFinished = false
        errorsAmount = 0
        changeImage(errorsAmount)
        wordToFind = getRandomWord()

        letters = CharArray(wordToFind.length)

        for (i in wordToFind.indices) {
            letters[i]  = '*'
        }
        binding.wordToGuess.text = String(letters)
    }

    fun clickStartBtn(view: View) {
        startGame()
    }
}