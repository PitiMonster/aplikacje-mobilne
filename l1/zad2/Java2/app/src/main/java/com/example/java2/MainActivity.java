package com.example.java2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private int answer = 0;
    private TextView respText;
    private TextView currRangeValText;
    private EditText numberInput;
    private Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        respText = findViewById(R.id.responseTextView);
        numberInput = findViewById(R.id.guessingNumInput);
        currRangeValText = findViewById(R.id.currentRangeValues);

        genNum(new View(this));
    }

    public void genNum(View view) {
        respText.setText("Wygenerowano nową liczbę");
        respText.setTextColor(Color.GRAY);
        numberInput.setText("");
        currRangeValText.setText("1 - 100");
        answer = rand.nextInt(100)+1;

    }

    public void checkButton(View view){

        if (numberInput.getText().toString().matches("")) {
            respText.setText("Wprowadź jakąś liczbę!");
            respText.setTextColor(Color.RED);
            return;
        }

        int num = Integer.parseInt(numberInput.getText().toString());
        String[] currentRangeArr = currRangeValText.getText().toString().split(" - ");
        String newCurrRange, newRespText;
        if (num < answer) {
            if (num < Integer.parseInt(currentRangeArr[0])) {
                newCurrRange = currentRangeArr[0] + " - " + currentRangeArr[1];
            } else {
                newCurrRange = num + " - " + currentRangeArr[1];
            }
            respText.setTextColor(Color.GREEN);
            newRespText = "Za mało!";
        } else if (num > answer) {
            if (num > Integer.parseInt(currentRangeArr[1])) {
                newCurrRange = currentRangeArr[0] + " - " + currentRangeArr[1];
            } else {
                newCurrRange = currentRangeArr[0] + " - " + num;
            }
            respText.setTextColor(Color.BLUE);
            newRespText = "Za dużo!";
        } else {
            newCurrRange = num + " - " + num;
            respText.setTextColor(Color.parseColor("#FF9933"));
            newRespText = "Zgadłeś!";
        }
        respText.setText(newRespText);
        currRangeValText.setText(newCurrRange);
        numberInput.setText("");
    }
}