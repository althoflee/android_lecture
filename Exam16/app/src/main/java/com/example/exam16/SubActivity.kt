package com.example.exam16

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


    private lateinit var mBtnGoHome: Button
    private lateinit var mTvMsg: TextView
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

        mBtnGoHome = findViewById(R.id.btn_go_home)
        mTvMsg = findViewById(R.id.tv_msg)
        mEdMsg = findViewById(R.id.ed_msg)

        val _message = intent.getStringExtra("message")
        mTvMsg.text = _message

        mBtnGoHome.setOnClickListener {
            val _resultIntent = Intent()
            _resultIntent.putExtra("message", mEdMsg.text.toString())

            setResult(RESULT_OK, _resultIntent)
            finish()

        }



    }
}