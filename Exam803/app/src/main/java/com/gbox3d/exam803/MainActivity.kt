package com.gbox3d.exam803

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream
import android.Manifest
import android.util.Log

class MainActivity : AppCompatActivity() {

    companion object {
        private const val WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1
    }

    lateinit var btnWrite:Button
    lateinit var edtSd:EditText

    private fun checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), WRITE_EXTERNAL_STORAGE_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // 권한이 부여됨
                Log.d("MainActivity","sd memory 권한이 부여됨")
            } else {
                // 권한 거부
                Log.d("MainActivity","sd memory 권한이 거부됨")
            }
        }
    }

    private fun isWritePermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnWrite = findViewById(R.id.btnWrite)
        edtSd = findViewById(R.id.editSD)

        checkAndRequestPermissions()

        btnWrite.setOnClickListener {
            //edt 내용을 읽어서 memo.txt sd 메모리에 저장한다.
            if (isWritePermissionGranted()) {
                writeToFile(edtSd.text.toString())
            } else {
                checkAndRequestPermissions()
            }
        }
    }

    private fun writeToFile(data: String) {
        val externalStorageVolumes: Array<out File> = ContextCompat.getExternalFilesDirs(applicationContext, null)
        val primaryExternalStorage = externalStorageVolumes[0]
        try {
            Log.d("MainActivity","write....")
            val file = File(primaryExternalStorage, "memo.txt")
            // 파일 경로 출력
            Log.d("FileLocation", "File saved at: ${file.absolutePath}")

            FileOutputStream(file).use { output ->
                output.write(data.toByteArray())

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}