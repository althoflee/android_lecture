package com.example.day0903;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    ActivityResultLauncher<Intent> launcher;

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

        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == RESULT_OK) {
//                        Intent _intent = getIntent();
                        Intent _intent = result.getData();
                        if(_intent != null) {
                            String _replace = _intent.getStringExtra("replace");
                            EditText _edName = findViewById(R.id.edName);
                            _edName.setText(_replace);
                        }
                    }
                }
        );

        findViewById(R.id.btnSend).setOnClickListener(v-> {

            Intent _intent = new Intent(this, MainActivity2.class);

            EditText _edName = findViewById(R.id.edName);

            _intent.putExtra("name", _edName.getText().toString());

            launcher.launch(_intent);

//            startActivity(_intent);
        });


    }
}