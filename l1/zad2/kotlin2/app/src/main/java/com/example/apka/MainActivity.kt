package com.example.apka

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

import com.example.apka.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var answer = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)
        genNum(view)
    }

    fun genNum(view: View) {
        binding.responseTextView.text = "Wygenerowano nową liczbę"
        binding.responseTextView.setTextColor(Color.GRAY)
        binding.guessingNumInput.text = null
        binding.currentRangeValues.text = "1 - 100"
        answer = Random.nextInt(1, 100)
    }

    fun checkButton(view: View){
        var num = binding.guessingNumInput.text.toString().toIntOrNull()
        var currentRangeArr = binding.currentRangeValues.text.split("\\s".toRegex())
        var newCurrRange: String
        var respText: String = when {
            num == null -> {
                newCurrRange = binding.currentRangeValues.text.toString()
                binding.responseTextView.setTextColor(Color.RED)
                "Wprowadź jakąś liczbę"
            }
            num < answer -> {
                newCurrRange = if (num < currentRangeArr[0].toInt()) {
                    "${currentRangeArr[0]} - ${currentRangeArr[2]}"
                } else {
                    "$num - ${currentRangeArr[2]}"
                }
                binding.responseTextView.setTextColor(Color.GREEN)
                "Za mało!"
            }
            num > answer -> {
                newCurrRange = if (num > currentRangeArr[2].toInt()) {
                    "${currentRangeArr[0]} - ${currentRangeArr[2]}"
                } else {
                    "${currentRangeArr[0]} - $num"
                }
                binding.responseTextView.setTextColor(Color.BLUE)
                "Za dużo!"
            }
            else -> {
                newCurrRange = "$num - $num"
                binding.responseTextView.setTextColor(Color.parseColor("#FF9933"))
                "Zgadłeś!"
            }
        }
        binding.responseTextView.text = respText
        binding.currentRangeValues.text = newCurrRange
        binding.guessingNumInput.text = null
    }
}