package com.example.tictactoe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.View
import android.widget.Toast
import com.example.tictactoe.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    val Play3x3: Int = 0;
    val Play5x5: Int = 1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)
    }

    fun play3x3(view: View) {
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("size", 3)
        startActivityForResult(intent, Play3x3)
    }

    fun play5x5(view: View) {
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("size", 5)
        startActivityForResult(intent, Play5x5)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            Play3x3 -> Toast.makeText(this, "Skończono gre 3x3!",  Toast.LENGTH_SHORT).show()
            Play5x5 -> Toast.makeText(this, "Skończono gre 5x5!",  Toast.LENGTH_SHORT).show()
            else -> Toast.makeText(this, "jestem else", Toast.LENGTH_SHORT).show()
        }
        if (data != null) {
            when(data.getIntExtra("winner", 2)) {
                0 -> Toast.makeText(this, "Wygrał gracz O!",  Toast.LENGTH_SHORT).show()
                1 -> Toast.makeText(this, "Wygrał gracz X!",  Toast.LENGTH_SHORT).show()
                2 -> Toast.makeText(this, "Remis!",  Toast.LENGTH_SHORT).show()
            }
        }

    }


}