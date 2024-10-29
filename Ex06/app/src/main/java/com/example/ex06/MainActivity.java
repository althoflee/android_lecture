package com.example.ex06;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

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

        ImageView image1 = findViewById(R.id.image_1);
        ImageView image2 = findViewById(R.id.image_2);

        //hide image
        image1.setVisibility(View.INVISIBLE);
        image2.setVisibility(View.INVISIBLE);

        findViewById(R.id.check_meat).setOnClickListener(v -> {
            Boolean isChecked = ((CheckBox) v).isChecked();
            if (isChecked) {
                image1.setVisibility(View.VISIBLE);
            } else {
                image1.setVisibility(View.INVISIBLE);
            }
        });

        findViewById(R.id.check_cheese).setOnClickListener(v -> {
            Boolean isChecked = ((CheckBox) v).isChecked();
            if (isChecked) {
                image2.setVisibility(View.VISIBLE);
            } else {
                image2.setVisibility(View.INVISIBLE);
            }
        });

    }
}