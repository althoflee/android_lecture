package com.gbox3d.ex1002

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SecondActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.second)

        val msg = intent.getStringExtra("msg")

        findViewById<Button>(R.id.btnReturn).setOnClickListener {
            var outIntent = Intent(this,MainActivity::class.java)
            outIntent.putExtra("msg","hello ${msg}")
            setResult(Activity.RESULT_OK,outIntent)
            Log.d("myLog","sendResult")
            finish()
        }

    }
}