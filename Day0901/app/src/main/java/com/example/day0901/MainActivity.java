package com.example.day0901;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RadioButton;

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

        findViewById(R.id.btnDisplayImage).setOnClickListener(v -> {
            ImageView imageView = findViewById(R.id.imgView);

            RadioButton btn223 = findViewById(R.id.rbtn_223);
            RadioButton btn41 = findViewById(R.id.rbtn_41);
            RadioButton btn44 = findViewById(R.id.rbtn_44);

            if (btn223.isChecked())
                imageView.setImageResource(R.drawable.image0);
            else if (btn41.isChecked())
                imageView.setImageResource(R.drawable.image1);
            else if (btn44.isChecked())
                imageView.setImageResource(R.drawable.image2);


//            imageView.setImageResource(R.drawable.image0);


        });
    }
}