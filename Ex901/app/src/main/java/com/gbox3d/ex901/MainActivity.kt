package com.gbox3d.ex901

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val graphicsView = myGraphicsView(this)

        val grpViewLayout =  findViewById<LinearLayout>(R.id.grapView)
        grpViewLayout.addView(graphicsView)

        findViewById<Button>(R.id.btnClear).setOnClickListener {
            graphicsView.clear()
        }

        findViewById<Button>(R.id.btnTest1).setOnClickListener {

            Log.d("myLog","click")

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = makeNetworkCall()
                    withContext(Dispatchers.Main) {
                        // 메인 스레드에서 UI 업데이트
                        Log.d("NetworkResponse", response)

                        try {
                            val jsonObject = JSONObject(response)
                            val result = jsonObject.getString("r")
                            val info = jsonObject.getString("info")

                            if (result == "ok") {
                                Toast.makeText(this@MainActivity, "success", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(this@MainActivity, info, Toast.LENGTH_LONG).show()
                            }
                        } catch (e: JSONException) {
                            // JSON 파싱 실패
                            Toast.makeText(this@MainActivity, "JSON 파싱 오류", Toast.LENGTH_LONG).show()
                            e.printStackTrace()
                        }
                    }
                } catch (e: Exception) {
                    // 네트워크 요청 실패
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, "네트워크 요청 오류", Toast.LENGTH_LONG).show()
                    }
                    e.printStackTrace()
                }
            }
        }

        findViewById<Button>(R.id.btnDownload).setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val url = URL("http://worweb.cafe24.com:8080/assets/mz01.jpg")
                    val connection = url.openConnection() as HttpURLConnection
                    connection.doInput = true
                    connection.connect()

                    val inputStream = connection.inputStream
                    val bitmap = BitmapFactory.decodeStream(inputStream)

                    val file = File(getExternalFilesDir(null) ,"/downloaded_image.jpg")
                    FileOutputStream(file).use { out ->
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                    }

                    withContext(Dispatchers.Main) {
                        //저장위치 로그 남기기
                        Log.d("myLog", "저장위치: ${file.absolutePath}")
                        Toast.makeText(this@MainActivity, "이미지 다운로드 및 저장 완료", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, "다운로드 실패: ${e.message}", Toast.LENGTH_SHORT).show()

                    }
                    e.printStackTrace()
                }
            }
        }

        findViewById<Button>(R.id.btnImg).setOnClickListener {
            ///storage/emulated/0/Android/data/com.gbox3d.ex901/files/downloaded_image.jpg
            val filePath = "${getExternalFilesDir(null)}/downloaded_image.jpg"
            graphicsView.loadImage(filePath)
        }
    }

    private fun writeToFile(data:String) {
        val file = File(getExternalFilesDir(null),"test.txt")

        try {
            FileOutputStream(file).use {output->
                output.write(data.toByteArray())
            }
        }
        catch (e:Exception) {
            e.printStackTrace()
        }
    }


    private suspend fun makeNetworkCall(uri:String = "api/v2/challenge"): String {
        return withContext(Dispatchers.IO) {
            // 네트워크 요청을 여기서 수행
            try {
                val url = URL("http://worweb.cafe24.com:8080/${uri}")
                val httpURLConnection = url.openConnection() as HttpURLConnection

                httpURLConnection.requestMethod = "GET"
                httpURLConnection.setRequestProperty("Content-Type", "text/plain")
                httpURLConnection.setRequestProperty("auth-token", "DtqBzT4O")

                if (httpURLConnection.responseCode == HttpURLConnection.HTTP_OK) {
                    httpURLConnection.inputStream.bufferedReader().use { it.readText() }
                } else {
                    "Response Code: ${httpURLConnection.responseCode}"
                }
            } catch (e: Exception) {
                e.printStackTrace()
                "Error: ${e.message}"
            }
        }
    }
}