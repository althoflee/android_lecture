package com.example.exam16

import android.content.Intent
import android.os.Bundle
import android.util.Log
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

    private lateinit var mbtnGoSub: Button
    private lateinit var medMsg : EditText
    private lateinit var mtvMsg : TextView

    private lateinit var mSubActivityResultLauncher: ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mSubActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult() ) { result ->
            if (result.resultCode == RESULT_OK) {
                // Handle the result
                Log.d("MainActivity", "SubActivity에서 돌아옴")
                val data: Intent? = result.data
                val message = data?.getStringExtra("message")
                Log.d("MainActivity", "message: $message")
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                mtvMsg.text = message
            } else {
                Log.d("MainActivity", "SubActivity에서 돌아오지 않음")
            }
        }



        mbtnGoSub = findViewById(R.id.btn_go_sub)
        mtvMsg = findViewById(R.id.tv_msg)
        medMsg = findViewById(R.id.ed_msg)


        mbtnGoSub.setOnClickListener {
            val intent = Intent(this, SubActivity::class.java)
            intent.putExtra("message", medMsg.text.toString())
            mSubActivityResultLauncher.launch(intent)
        }

    }
}