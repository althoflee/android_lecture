package com.example.day0802;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    ImageView image_meat, image_cheese;

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
        image_meat = findViewById(R.id.image_meat);
        image_cheese = findViewById(R.id.image_cheese);
        //숨기기
        image_meat.setVisibility(View.INVISIBLE);
        image_cheese.setVisibility(View.INVISIBLE);

        CheckBox _checkMeat = findViewById(R.id.check_meat);

        _checkMeat.setOnClickListener((chbkbtn)-> {

            Boolean isChecked = _checkMeat.isChecked();

            if(isChecked) {
                image_meat.setVisibility(View.VISIBLE);
            } else {
                image_meat.setVisibility(View.INVISIBLE);
            }
        });

        findViewById(R.id.check_cheese).setOnClickListener(v-> {
            Boolean isChecked = ((CheckBox)v).isChecked();
            if(isChecked) {
                image_cheese.setVisibility(View.VISIBLE);
            } else {
                image_cheese.setVisibility(View.INVISIBLE);
            }
        });

        View _mainLayout = findViewById(R.id.main);

        findViewById(R.id.rbtnRed).setOnClickListener(v-> {
            Boolean isChecked = ((RadioButton)v).isChecked();
            if(isChecked)
                _mainLayout.setBackgroundColor(Color.rgb(255, 0, 0));
        });

        findViewById(R.id.rbtnBlue).setOnClickListener(v-> {
            Boolean isChecked = ((RadioButton)v).isChecked();
            if(isChecked)
                _mainLayout.setBackgroundColor(Color.rgb(0, 0, 255));

        });
    }
}