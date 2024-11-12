package com.example.ex13;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
//import android.widget.Toolbar;
// 올바른 import를 추가합니다.
import androidx.appcompat.widget.Toolbar;

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

        // androidx.appcompat.widget.Toolbar를 사용해야 오류가 발생하지 않습니다.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 상위 클래스의 메서드 호출
        super.onCreateOptionsMenu(menu);

        // MenuInflater를 통해 메뉴 XML을 확장
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mymenu, menu); // 메뉴 리소스(myemenu.xml)를 확장하여 메뉴에 추가

        return true; // true를 반환하여 메뉴가 표시되도록 설정
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.blue) {
            // 파랑색 선택 시 동작
            Toast.makeText(this, "파랑색 선택", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.green) {
            // 초록색 선택 시 동작
            Toast.makeText(this, "초록색 선택", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

}