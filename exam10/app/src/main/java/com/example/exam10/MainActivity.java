package com.example.exam10;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    // Activity Result Launcher 정의
    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // SecondActivity에서 전달된 데이터를 가져옴
                    Intent data = result.getData();
                    if (data != null) {
                        String returnedData = data.getStringExtra("returnedData");
                        // 데이터를 사용하는 코드 추가 (예: 텍스트뷰에 표시)
//                        TextView textView = findViewById(R.id.textView);
//                        textView.setText(returnedData);

                        EditText edName = findViewById(R.id.edName);
                        edName.setText(returnedData);
                    }
                }
            }
    );

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

        findViewById(R.id.btnGotoSecond).setOnClickListener(v -> {
            Intent intent = new Intent(this, SecondActivity.class);

            EditText edName = findViewById(R.id.edName);

            intent.putExtra("name", edName.getText().toString());
            launcher.launch(intent);
        });

        findViewById(R.id.btnMap).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:37.5662952,126.9779451"));

            startActivity(intent);
        });
        }



}
