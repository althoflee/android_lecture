package com.example.ex13;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

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

        TextView txCounter = findViewById(R.id.txCounter);
        txCounter.setText("0");

        Button btnInc = findViewById(R.id.btnInc);
        btnInc.setOnClickListener(v -> {
            int count = Integer.parseInt(txCounter.getText().toString());
            txCounter.setText(String.valueOf(++count));
        });

        if(savedInstanceState != null) {
            txCounter.setText(savedInstanceState.getString("count"));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        TextView txCounter = findViewById(R.id.txCounter);
        outState.putString("count", txCounter.getText().toString());
    }
}