package com.example.exam18

import android.icu.text.IDNA.Info
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Website.URL
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
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var mBtnTest: Button
    private lateinit var mtvInfo: TextView

    private lateinit var medName: EditText
    private lateinit var medStudentId : EditText
    private lateinit var medPasswd : EditText
    private lateinit var mbtnRegister : Button

    private lateinit var mbtnStartGame : Button
    private lateinit var medGuess : EditText
    private lateinit var mbtnSendNumber : Button



    private val urlString = "http://cbhai01.iptime.org:17870/api/v2/challenge"

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
        mtvInfo = findViewById(R.id.tvInfo)

        medName = findViewById(R.id.edName)
        medStudentId = findViewById(R.id.edStudentId)
        medPasswd = findViewById(R.id.edPasswd)
        mbtnRegister = findViewById(R.id.btnReg)

        mbtnStartGame = findViewById(R.id.btnStartGame)
        medGuess = findViewById(R.id.edGuess)
        mbtnSendNumber = findViewById(R.id.btnSendNumber)

        // 게임 시작 버튼 클릭 이벤트
        mbtnStartGame.setOnClickListener {
            val studentId = medStudentId.text.toString()
            val passwd = medPasswd.text.toString()

            mtvInfo.text = "게임 시작 요청 중..."

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    // Start game API 호출
                    val startGameUrl = "$urlString/start_hl?studentId=$studentId&passwd=$passwd"
                    val url = URL(startGameUrl)
                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "GET"
                    connection.setRequestProperty("Content-Type", "text/plain")
                    connection.setRequestProperty("auth-token", "DtqBzT4O")

                    val responseCode = connection.responseCode
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        val inputStream = BufferedReader(InputStreamReader(connection.inputStream))
                        val response = StringBuilder()
                        var line: String?

                        while (inputStream.readLine().also { line = it } != null) {
                            response.append(line)
                        }

                        inputStream.close()
                        println("Response: $response")

                        val jsonResponse = JSONObject(response.toString())
                        val result = jsonResponse.getString("r")
                        val message = jsonResponse.getString("info")

                        if(result == "ok") {
                            withContext(Dispatchers.Main) {
                                mtvInfo.text = "게임 시작 성공: $message"
                            }
                        }
                        else {
                            withContext(Dispatchers.Main) {
                                mtvInfo.text = "게임 시작 실패: $message"
                            }
                        }


                    } else {
                        println("GET request failed: Response code $responseCode")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        mtvInfo.text = "게임 시작 실패"
                    }
                }
            }
        }

        // 숫자 추측 버튼 클릭 이벤트
        mbtnSendNumber.setOnClickListener {
            val studentId = medStudentId.text.toString()
            val passwd = medPasswd.text.toString()
            val guess = medGuess.text.toString()

            if (guess.isBlank()) {
                mtvInfo.text = "숫자를 입력하세요."
                return@setOnClickListener
            }

            mtvInfo.text = "숫자 추측 요청 중..."

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    // Find game API 호출
                    val findGameUrl = "$urlString/find_hl?studentId=$studentId&passwd=$passwd&num=$guess"
                    val url = URL(findGameUrl)
                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "GET"
                    connection.setRequestProperty("Content-Type", "text/plain")
                    connection.setRequestProperty("auth-token", "DtqBzT4O")

                    val responseCode = connection.responseCode
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        val inputStream = BufferedReader(InputStreamReader(connection.inputStream))
                        val response = StringBuilder()
                        var line: String?

                        while (inputStream.readLine().also { line = it } != null) {
                            response.append(line)
                        }

                        inputStream.close()
                        println("Response: $response")

                        val jsonResponse = JSONObject(response.toString())
                        val result = jsonResponse.getString("r")
                        val message = jsonResponse.getString("info")

                        withContext(Dispatchers.Main) {
                            mtvInfo.text = "결과: $result\n메시지: $message"
                        }
                    } else {
                        println("GET request failed: Response code $responseCode")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        mtvInfo.text = "숫자 추측 실패"
                    }
                }
            }
        }

        mbtnRegister.setOnClickListener {
            val name = medName.text.toString()
            val studentId = medStudentId.text.toString()
            val passwd = medPasswd.text.toString()

            // register?name=이석준&studentId=60695&passwd=1234
            // 서버로 데이터를 전송
            // GET 요청을 통해 서버에 데이터를 전송
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    // GET 요청 URL 구성
                    val registerUrl =
                        "$urlString/register?name=${name}&studentId=${studentId}&passwd=${passwd}&classId=2024_01"
                    val url = URL(registerUrl)
                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "GET"
                    connection.setRequestProperty("Content-Type", "text/plain")
                    connection.setRequestProperty("auth-token", "DtqBzT4O")

                    val responseCode = connection.responseCode //서버 응답을 기다는 부분
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        val inputStream = BufferedReader(InputStreamReader(connection.inputStream))
                        val response = StringBuilder()
                        var line: String?

                        while (inputStream.readLine().also { line = it } != null) {
                            response.append(line)
                        }

                        inputStream.close()
                        println("Response: $response")

                        // JSON 응답 파싱
                        val jsonResponse = JSONObject(response.toString())
                        val result = jsonResponse.getString("r")
                        val message = jsonResponse.getString("info")

                        withContext(Dispatchers.Main) {
                            mtvInfo.text = "Registration Result: $result\nMessage: $message"
                        }
                    } else {
                        println("GET request failed: Response code $responseCode")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    withContext(Dispatchers.Main) {
                        mbtnRegister.isEnabled = true
                    }
                }
            }



        }

        mBtnTest.setOnClickListener {
            v->
            val _v = v as Button
            //GET http://cbhai01.iptime.org:17870/api/v2/challenge
            //Content-Type: text/plain
            //auth-token : DtqBzT4O
            _v.text = "wait..."
            _v.isEnabled = false
            mtvInfo.text = "connetting..."

            CoroutineScope(Dispatchers.IO).launch {
                try {
//                    sendGetRequest()
                    val urlString = "$urlString"
                    val url = URL(urlString)
                    val connection = url.openConnection() as HttpURLConnection

                    try {
                        connection.requestMethod = "GET"
                        connection.setRequestProperty("Content-Type", "text/plain")
                        connection.setRequestProperty("auth-token", "DtqBzT4O")

                        val responseCode = connection.responseCode
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            val inputStream = BufferedReader(InputStreamReader(connection.inputStream))
                            val response = StringBuilder()
                            var line: String?

                            while (inputStream.readLine().also { line = it } != null) {
                                response.append(line)
                            }

                            inputStream.close()
                            println("Response: $response")

                            // JSON 파싱
                            val jsonResponse = JSONObject(response.toString())
                            val result = jsonResponse.getString("r")
                            val info = jsonResponse.getString("info")

                            withContext(Dispatchers.Main) {
                                mtvInfo.text = "Result : $result\nInfo:${info}"
                            }


                        } else {
                            println("GET request failed: Response code $responseCode")
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        connection.disconnect()
                    }
                }
                finally {
                    withContext(Dispatchers.Main) {
                        _v.text = "test"
                        _v.isEnabled = true
                    }
                }
            }
        }
    }


}