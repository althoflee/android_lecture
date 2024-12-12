package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

private lateinit var radioGroup: RadioGroup
private lateinit var btnCalculate: Button
private lateinit var radio_add: RadioButton
private lateinit var radio_subtract: RadioButton
private lateinit var radio_multiply: RadioButton
private lateinit var radio_divide: RadioButton
private lateinit var dlgLauncher: ActivityResultLauncher<Intent>
private lateinit var btnCal: Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnCal = findViewById<Button>(R.id.btnCalculate)
        btnCal.setOnClickListener {
            val _inputIntent = Intent(this, dlg_cal::class.java)
            _inputIntent.putExtra("msg", radioGroup.toString())
            dlgLauncher.launch(_inputIntent)

        }
    }
}