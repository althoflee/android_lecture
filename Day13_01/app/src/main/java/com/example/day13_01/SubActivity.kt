package com.example.day13_01

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SubActivity : AppCompatActivity() {
    private lateinit var mBtnGotoHome: Button
    private lateinit var mtvMsg: TextView
    private lateinit var mEdMsg: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sub)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mtvMsg = findViewById(R.id.tvMsg)
        mEdMsg = findViewById(R.id.edMsg)

        val _msg = intent.getStringExtra("message")
        mtvMsg.text = _msg

        mBtnGotoHome = findViewById(R.id.btnGotoHome)
        mBtnGotoHome.setOnClickListener {
            val _resultIntent = Intent()
            _resultIntent.putExtra("msg", mEdMsg.text.toString())
            setResult(RESULT_OK, _resultIntent)
            finish()
        }

    }
}