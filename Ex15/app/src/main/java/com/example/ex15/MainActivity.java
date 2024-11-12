package com.example.ex15;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

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

        Button selectDateButton = findViewById(R.id.select_date_button);
        Button selectTimeButton = findViewById(R.id.select_time_button);
        EditText selectedDateText = findViewById(R.id.selected_date_text);
        EditText selectedTimeText = findViewById(R.id.selected_time_text);

        selectDateButton.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        // 선택된 날짜를 EditText에 표시
                        selectedDateText.setText("선택된 날짜: " + selectedYear + "/" + (selectedMonth + 1) + "/" + selectedDay);
                    }, year, month, day);

            datePickerDialog.show();

        });

        selectTimeButton.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    (view, selectedHour, selectedMinute) -> {
                        // 선택된 시간을 EditText에 표시
                        selectedTimeText.setText("선택된 시간: " + selectedHour + ":" + String.format("%02d", selectedMinute));
                    }, hour, minute, true);

            timePickerDialog.show();

        });

        Button btnAlertYesNo = findViewById(R.id.btn_alert_yes_no);

        btnAlertYesNo.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("제목");
            builder.setMessage("메시지");

            builder.setPositiveButton("예", (dialog, which) -> {
                // 예 버튼 클릭 시 실행할 코드
                Toast.makeText(this, "예 버튼 클릭", Toast.LENGTH_SHORT).show();
            });

            builder.setNegativeButton("아니오", (dialog, which) -> {
                // 아니오 버튼 클릭 시 실행할 코드
                Toast.makeText(this, "아니오 버튼 클릭", Toast.LENGTH_SHORT).show();
            });

            AlertDialog dialog = builder.create();
            dialog.show();

        });

        Button btnAlertList = findViewById(R.id.btn_alert_list);

        btnAlertList.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("제목");
            builder.setItems(new String[]{"항목1", "항목2", "항목3"}, (dialog, which) -> {

                switch (which) {
                    case 0:
                        // 항목1 선택 시 동작
                        Toast.makeText(this, "항목1 선택됨", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        // 항목2 선택 시 동작
                        Toast.makeText(this, "항목2 선택됨", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        // 항목3 선택 시 동작
                        Toast.makeText(this, "항목3 선택됨", Toast.LENGTH_SHORT).show();
                        break;
                }


            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        Button btnCustomDialog = findViewById(R.id.btn_custom_dialog);
        btnCustomDialog.setOnClickListener(v -> {

            // custom_dialog.xml을 인플레이션
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.custom_dialog, null);

            // 대화 상자 빌더 설정
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(dialogView);

            // 다이얼로그 요소 참조
            EditText usernameEditText = dialogView.findViewById(R.id.edtName);
            Button loginButton = dialogView.findViewById(R.id.btnOk);
            AlertDialog dialog = builder.create();

            // 로그인 버튼 클릭 이벤트 설정
            loginButton.setOnClickListener(view -> { // 매개변수 이름을 'view'로 변경
                String username = usernameEditText.getText().toString();
                Toast.makeText(this, "Username: " + username, Toast.LENGTH_SHORT).show();
                dialog.dismiss(); // 대화 상자 닫기
            });

            dialog.show();
        });


    }


}