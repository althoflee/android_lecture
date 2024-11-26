package com.example.day13_02

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var mBtnShowDlg: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mBtnShowDlg = findViewById(R.id.btn_show_dlg)
        mBtnShowDlg.setOnClickListener {
            _ShowDlg()

        }
    }

    private fun _ShowDlg() {
        val dlgView =
            LayoutInflater.from(this).inflate(R.layout.custom_dlg, null)
        val dlg = AlertDialog.Builder(this)
            .setView(dlgView)
            .create()
        val btnOk = dlgView.findViewById<Button>(R.id.btnOk)
        val btnCancel = dlgView.findViewById<Button>(R.id.btnCancel)
        btnOk.setOnClickListener {
            dlg.dismiss()
            Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show()
        }
        btnCancel.setOnClickListener {
            dlg.dismiss()
            Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show()
        }

        dlg.show()

    }
}