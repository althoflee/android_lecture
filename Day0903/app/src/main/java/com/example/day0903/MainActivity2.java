package com.example.day0903;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent _intent = getIntent();
        if(_intent != null) {
            String _name = _intent.getStringExtra("name");
            EditText _edReplace = findViewById(R.id.edReplace);
            _edReplace.setText(_name);
        }

        findViewById(R.id.btnBack).setOnClickListener(v-> {

            EditText _edReplace = findViewById(R.id.edReplace);

            Intent resultIntent = new Intent();

            resultIntent.putExtra("replace", "test00000");
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}