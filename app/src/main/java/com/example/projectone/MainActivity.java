package com.example.projectone;

import android.content.Intent; // подключаем класс Intent
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View; // подключаем класс View для обработки нажатия кнопки
import android.widget.Button;
import android.widget.EditText; // подключаем класс EditText
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button play, ai;
    EditText width, height;
    int w, h;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        play = findViewById(R.id.playButton);
        ai = findViewById(R.id.aiButton);
        width = findViewById(R.id.hSize);
        height = findViewById(R.id.vSize);
    }

    public void startGame(View v) {
        Intent intent = new Intent(this, ActivityTwo.class);
        if (width.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Выберите размеры поля", Toast.LENGTH_SHORT).show();
        } else {
            w = Integer.parseInt(width.getText().toString());
            h = w;
            if (w > 2) {
                //h = Integer.parseInt(height.getText().toString());
                intent.putExtra("width", w);
                intent.putExtra("height", h);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Размеры поля слишком малы", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void aiStartGame(View view) {
        Intent intent = new Intent(this, AiActivity.class);
        if (width.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Выберите размеры поля", Toast.LENGTH_SHORT).show();
        } else {
            w = Integer.parseInt(width.getText().toString());
            h = w;
            if (w > 2) {
                //h = Integer.parseInt(height.getText().toString());
                intent.putExtra("width", w);
                intent.putExtra("height", h);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Размеры поля слишком малы", Toast.LENGTH_SHORT).show();
            }
        }
    }
}


