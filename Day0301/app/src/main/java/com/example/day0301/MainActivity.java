package com.example.day0301;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }

    public void generateRandomNumber(View view) {
        Random rand = new Random();
        int randomNum = rand.nextInt(100); //0 ~ 99 까지 난수 생성

        TextView tv = findViewById(R.id.tvRandomNumber);
        tv.setText("난수는 : " + randomNum);

        Log.d("MainActivity", "난수는 : " + randomNum);

    }
}