package com.example.day10_1;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.PopupMenu;
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

        Button btnPopup = findViewById(R.id.btnPopupMenu);
        btnPopup.setOnClickListener(v-> {
            PopupMenu popup = new PopupMenu(this, v);
            popup.getMenuInflater().inflate(R.menu.popup_menu,popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if(id == R.id.red) {
                    Toast.makeText(this,"Red",Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.blue) {
                    Toast.makeText(this,"Blue",Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.green) {
                    Toast.makeText(this,"Green",Toast.LENGTH_SHORT).show();
                }
                return false;
            });
            popup.show();

        });

        TextView txOutput = findViewById(R.id.txOutput);

        Button btnDate = findViewById(R.id.btnDateDlg);
        btnDate.setOnClickListener(v-> {
            DatePickerDialog dateDlg = new DatePickerDialog(this);
            dateDlg.setOnDateSetListener((_v, year, month, dayOfMonth) -> {
                txOutput.setText(year+"-"+(month+1)+"-"+dayOfMonth);
            });
            dateDlg.show();
        });

        Button btnAlert = findViewById(R.id.btnAlert);
        btnAlert.setOnClickListener(v-> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("알림");
            builder.setMessage("진짜로?");
            builder.setPositiveButton("yes", (dialog, which) -> {
                Toast.makeText(this,"yes",Toast.LENGTH_SHORT).show();
            });
            builder.setNegativeButton("no", (dialog, which) -> {
                Toast.makeText(this,"no",Toast.LENGTH_SHORT).show();
            });

            AlertDialog _dlg = builder.create();
            _dlg.show();

        });



    }
}