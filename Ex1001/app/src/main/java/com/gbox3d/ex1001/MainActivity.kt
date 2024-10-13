package com.gbox3d.ex1001

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    lateinit var btnPing:Button

    lateinit var edtId:EditText
    lateinit var edtPasswd:EditText
    lateinit var edtAnswer:EditText
    lateinit var btnJoin:Button

    private suspend fun makeNetworkCall(
        baseAddr:String = "http://worweb.cafe24.com:8080",
        uri:String = "api/v2/challenge",
        requestMethod:String="GET"): String {

        return withContext(Dispatchers.IO) {
            // 네트워크 요청을 여기서 수행
            try {
                val url = URL("${baseAddr}/${uri}")
                val httpURLConnection = url.openConnection() as HttpURLConnection

                httpURLConnection.requestMethod = requestMethod
                httpURLConnection.setRequestProperty("Content-Type", "text/plain")
                httpURLConnection.setRequestProperty("auth-token", "DtqBzT4O")

                if (httpURLConnection.responseCode == HttpURLConnection.HTTP_OK) {
                    httpURLConnection.inputStream.bufferedReader().use { it.readText() }
                } else {
                    "{\"r\":\"error\",\"code\":${httpURLConnection.responseCode}}"
                }
            } catch (e: Exception) {
                e.printStackTrace()
                "{\"r\":\"failed\",\"info\":\"connection error\"}"
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnPing = findViewById(R.id.btnPing)
        edtId = findViewById(R.id.edtID)
        edtPasswd = findViewById(R.id.edtPasswd)
        edtAnswer = findViewById(R.id.edtAnswer)

        btnJoin = findViewById(R.id.btnJoin)

        btnPing.setOnClickListener {

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


                            if (result == "ok") {
                                val info = jsonObject.getString("info")
                                Toast.makeText(this@MainActivity, "success ${info}", Toast.LENGTH_LONG).show()
                            }
                            else if(result == "failed") {
                                val info = jsonObject.getString("info")
                                Toast.makeText(this@MainActivity, "failed ${info}", Toast.LENGTH_LONG).show()

                            }
                            else {
                                val code = jsonObject.getInt("code")
                                Toast.makeText(this@MainActivity, "res : ${code}", Toast.LENGTH_LONG).show()

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


        /*
### challenge register
GET http://localhost:8080/api/v2/challenge/register?name=이석준&studentId=60695&passwd=1234
Content-Type: text/plain
auth-token : DtqBzT4O

### start hl game
GET http://localhost:8080/api/v2/challenge/start_hl?studentId=60695&passwd=1234
Content-Type: text/plain
auth-token : DtqBzT4O

### find hl game
GET http://localhost:8080/api/v2/challenge/find_hl?studentId=60695&passwd=1234&num=8765
Content-Type: text/plain
auth-token : DtqBzT4O
         */

        btnJoin.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = makeNetworkCall(
                        uri = "api/v2/challenge/register?name=${edtId.text}&studentId=${edtId.text}&passwd=${edtPasswd.text}"
                    )
                    withContext(Dispatchers.Main) {
                        // 메인 스레드에서 UI 업데이트
                        Log.d("NetworkResponse", response)

                        try {
                            val jsonObject = JSONObject(response)
                            val result = jsonObject.getString("r")

                            if (result == "ok") {
                                val info = jsonObject.getString("info")
                                Toast.makeText(this@MainActivity, "success ${info}", Toast.LENGTH_LONG)
                                    .show()
                            } else {
                                val code = jsonObject.getInt("code")
                                if(code == 409)
                                    Toast.makeText(this@MainActivity, "이미 존재합니다.", Toast.LENGTH_LONG).show()
                                else {
                                    Toast.makeText(this@MainActivity, "res : ${code}", Toast.LENGTH_LONG).show()
                                }
                            }
                        } catch (e: JSONException) {
                            // JSON 파싱 실패
                            Toast.makeText(this@MainActivity, "JSON 파싱 오류", Toast.LENGTH_LONG)
                                .show()
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

        findViewById<Button>(R.id.btnCreate).setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = makeNetworkCall(
                        uri = "api/v2/challenge/start_hl?studentId=${edtId.text}&passwd=${edtPasswd.text}"
                    )
                    withContext(Dispatchers.Main) {
                        // 메인 스레드에서 UI 업데이트
                        Log.d("NetworkResponse", response)

                        try {
                            val jsonObject = JSONObject(response)
                            val result = jsonObject.getString("r")

                            if (result == "ok") {
                                val info = jsonObject.getString("info")
                                Toast.makeText(this@MainActivity, "success ${info}", Toast.LENGTH_LONG)
                                    .show()
                            } else {
                                val code = jsonObject.getInt("code")
                                if(code == 409)
                                    Toast.makeText(this@MainActivity, "사용자정보를 잘못입력하셨습니다.", Toast.LENGTH_LONG).show()
                                else {
                                    Toast.makeText(this@MainActivity, "res : ${code}", Toast.LENGTH_LONG).show()
                                }
                            }
                        } catch (e: JSONException) {
                            // JSON 파싱 실패
                            Toast.makeText(this@MainActivity, "JSON 파싱 오류", Toast.LENGTH_LONG)
                                .show()
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

        findViewById<Button>(R.id.btnAnswer).setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = makeNetworkCall(
                        uri = "api/v2/challenge/find_hl?studentId=${edtId.text}&passwd=${edtPasswd.text}&num=${edtAnswer.text}"
                    )
                    withContext(Dispatchers.Main) {
                        // 메인 스레드에서 UI 업데이트
                        Log.d("NetworkResponse", response)
                        //{"r":"ok","info":"정답보다 큽니다.","dir":-1}
                        if(response.contains("r")) {
                            try {
                                val jsonObject = JSONObject(response)
                                val result = jsonObject.getString("r")

                                if (result == "ok") {
                                    val dir = jsonObject.getInt("dir")
                                    if(dir == 0) {
                                        Toast.makeText(this@MainActivity, "정답입니다.", Toast.LENGTH_LONG)
                                            .show()
                                    }
                                    else if(dir == 1) {
                                        Toast.makeText(this@MainActivity, "정답보다 작습니다.", Toast.LENGTH_LONG)
                                            .show()
                                    }
                                    else {
                                        Toast.makeText(this@MainActivity, "정답보다 큽니다.", Toast.LENGTH_LONG)
                                            .show()
                                    }


                                    // val info = jsonObject.getString("info")
                                    // Toast.makeText(this@MainActivity, "success ${info}", Toast.LENGTH_LONG)
//                                        .show()
                                } else {
                                    val code = jsonObject.getInt("code")
                                    if(code == 409)
                                        Toast.makeText(this@MainActivity, "사용자정보를 잘못입력하셨습니다.", Toast.LENGTH_LONG).show()
                                    else {
                                        Toast.makeText(this@MainActivity, "res : ${code}", Toast.LENGTH_LONG).show()
                                    }
                                }
                            } catch (e: JSONException) {
                                // JSON 파싱 실패
                                Toast.makeText(this@MainActivity, "JSON 파싱 오류", Toast.LENGTH_LONG)
                                    .show()
                                e.printStackTrace()
                            }
                        }
                        else {
                            Toast.makeText(this@MainActivity, response, Toast.LENGTH_LONG).show()
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

    }
}