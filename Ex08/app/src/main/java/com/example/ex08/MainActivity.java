package com.example.ex08;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Switch;

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

        ImageView _light = findViewById(R.id.image);
        _light.setImageResource(R.drawable.off);

        Switch _switch = findViewById(R.id.switch1);

        _switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                _light.setImageResource(R.drawable.on);
            } else {
                _light.setImageResource(R.drawable.off);
                }
        });
    }
}