package com.example.day14_01

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var mBtnTest: Button
    private lateinit var mTvInfo: TextView

    private lateinit var mEdStudentId: EditText
    private lateinit var mEdName: EditText
    private lateinit var mEdPasswd: EditText
    private lateinit var mBtnRegister: Button

    private val mBaseUrl = "http://cbhai01.iptime.org:17870/api/v2/challenge"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mBtnTest = findViewById(R.id.btnTest)
        mTvInfo = findViewById(R.id.tvInfo)

        mEdStudentId = findViewById(R.id.edStudentId)
        mEdName = findViewById(R.id.edName)
        mEdPasswd = findViewById(R.id.edPasswd)
        mBtnRegister = findViewById(R.id.btnRegister)

        mBtnRegister.setOnClickListener {
            val studentId = mEdStudentId.text.toString()
            val name = mEdName.text.toString()
            val passwd = mEdPasswd.text.toString()

        }


        mBtnTest.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {
                val url = URL(mBaseUrl)
                val connection = url.openConnection() as HttpURLConnection

                try {
                    connection.requestMethod = "GET"
                    connection.setRequestProperty("Content-Type", "text/plain")
                    connection.setRequestProperty("auth-token","DtqBzT4O")

                    val responseCode = connection.responseCode

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        val inputStream = BufferedReader(InputStreamReader(connection.inputStream))
                        val response = StringBuilder()
                        var line:String?

                        while(inputStream.readLine().also { line = it } != null) {
                            response.append(line)
                        }
                        inputStream.close()

                        Log.d("MainActivity", response.toString())
                        withContext(Dispatchers.Main) {
                            mTvInfo.text = response.toString()
                        }
                    }
                    else {
                        Log.d("MainActivity", "Error: $responseCode")
                    }

                }
                catch (e: Exception) {
                    e.printStackTrace()
                }
                finally {
                    connection.disconnect()
                }

            }

        }

    }
}