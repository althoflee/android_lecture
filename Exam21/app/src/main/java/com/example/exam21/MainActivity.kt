package com.example.exam21

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var mbtnSend: Button
    private lateinit var medMsg: EditText
    private lateinit var mtvMsg: TextView

    private lateinit var mSubActivityLaucher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mbtnSend = findViewById(R.id.btnSend)
        medMsg = findViewById(R.id.edMsg)
        mtvMsg = findViewById(R.id.tvMsg)

        mSubActivityLaucher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            result ->
            if (result.resultCode == RESULT_OK) {
                val msg = result.data?.getStringExtra("msg")


                mtvMsg.text = "${medMsg.text.toString()} ${msg}"

//                medMsg.setText(medMsg.text.toString() + msg)
//                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

            }
        }


        mbtnSend.setOnClickListener {
            val msg = medMsg.text.toString()
            val _intent = Intent(this, SubActivity::class.java)
            _intent.putExtra("msg", msg)
            mSubActivityLaucher.launch(_intent)
        }

    }
}