package com.example.day0801;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        View _colorLayout = findViewById(R.id.imageView);

        findViewById(R.id.btnRed).setOnClickListener( view -> {
            _colorLayout.setBackgroundColor(Color.rgb(255, 0, 0));
        } );

        findViewById(R.id.btnBlue).setOnClickListener( view -> {
            _colorLayout.setBackgroundColor(Color.rgb(0, 0, 255));
        });
    }
}