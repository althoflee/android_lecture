package com.example.day06;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
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

        Log.d("mytag", "onCreate");

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        findViewById(R.id.btnHello).setOnClickListener(v -> {
            Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
            Log.d("mytag", "Hello");
            TextView tvhello = findViewById(R.id.tvHello);
            tvhello.setText("Hello");
        });

        findViewById(R.id.btnSend).setOnClickListener(view -> {
            TextView tvhello = findViewById(R.id.tvHello);
            EditText etMsg = findViewById(R.id.etMsg);
            tvhello.setText(etMsg.getText());
        });
    }
}