package com.example.ex01;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

        Log.d("mytag", "onCreate");


        Button btnHello = findViewById(R.id.btnHello);
        TextView tvHello = findViewById(R.id.tvHello);

        btnHello.setOnClickListener(v -> {
            Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
            tvHello.setText("Hello");
        });


    }

    public void onClickTest(View v) {
        TextView tvHello = findViewById(R.id.tvHello);
        tvHello.setText("Test");
        Toast.makeText(this, "Test", Toast.LENGTH_SHORT).show();
        Log.d("mytag", "test");
    }
}

