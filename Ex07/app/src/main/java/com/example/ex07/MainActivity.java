 package com.example.ex07;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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

        View mainLayout = findViewById(R.id.main);
        RadioButton red = findViewById(R.id.red);
        RadioButton blue = findViewById(R.id.blue);

//        findViewById(R.id.red).setOnClickListener(v -> mainLayout.setBackgroundColor(0xFFFF0000));
//        findViewById(R.id.blue).setOnClickListener(v -> mainLayout.setBackgroundColor(0xFF0000FF));

        findViewById(R.id.change).setOnClickListener(v -> {
            if (red.isChecked()) {
                mainLayout.setBackgroundColor(0xFFFF0000);
            } else if (blue.isChecked()) {
                mainLayout.setBackgroundColor(0xFF0000FF);
            }
        });

    }
}