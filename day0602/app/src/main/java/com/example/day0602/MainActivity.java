package com.example.day0602;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
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

        Button btnPlus = findViewById(R.id.btnPlus);
        EditText etNum1 = findViewById(R.id.etNum1);
        EditText etNum2 = findViewById(R.id.etNum2);
        TextView tvResult = findViewById(R.id.tvResult);

        btnPlus.setOnClickListener(v-> {
            Log.d("mytag", "btnPlus Clicked");
            String num1 = etNum1.getText().toString();
            String num2 = etNum2.getText().toString();
//            int result = num1 + num2;
            tvResult.setText("reault :" +
                    (Integer.parseInt(num1) +
                    Integer.parseInt(num2)) );
        });
    }
}