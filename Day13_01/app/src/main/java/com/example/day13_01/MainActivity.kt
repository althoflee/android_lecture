package com.example.day13_01

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var mBtnGoSub: Button
    private lateinit var mTvMsg: TextView
    private lateinit var mEdMsg: EditText
    private lateinit var mSubActivityResultLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mBtnGoSub = findViewById(R.id.btn_go_sub)
        mTvMsg = findViewById(R.id.tvMsg)
        mEdMsg = findViewById(R.id.edMsg)

        mSubActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                val msg = data?.getStringExtra("msg")
                mTvMsg.text = msg
            }

        }

        mBtnGoSub.setOnClickListener {
            val _intent = Intent(this,
                SubActivity::class.java)
            _intent.putExtra("message", mEdMsg.text.toString())
            mSubActivityResultLauncher.launch(_intent)

        }

    }
}