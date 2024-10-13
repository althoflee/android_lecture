package com.gbox3d.ex1002

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {

    // ActivityResultLauncher 초기화
    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            // 결과 처리
            val message = intent?.getStringExtra("msg")
            Log.d("myLog", "Received message: $message")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnGoSecond).setOnClickListener {
            var intent = Intent(this,SecondActivity::class.java)

            Log.d("myLog","send Request")

            intent.putExtra("msg","Doromith")


            startForResult.launch(intent)
        }

    }

}