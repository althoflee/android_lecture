package com.example.exam21

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SubActivity : AppCompatActivity() {

    private lateinit var mbtnSend: Button
    private lateinit var medMsg: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sub)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mbtnSend = findViewById(R.id.btnSend)
        medMsg = findViewById(R.id.edMsg)

        mbtnSend.setOnClickListener {
            val msg = medMsg.text.toString()
            val _resultIntent = Intent()
            _resultIntent.putExtra("msg", msg)
            setResult(RESULT_OK, _resultIntent)
            finish()
        }
    }
}