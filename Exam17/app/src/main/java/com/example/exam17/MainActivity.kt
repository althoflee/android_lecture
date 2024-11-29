package com.example.exam17

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.zip.Inflater

class MainActivity : AppCompatActivity() {

    private lateinit var mBtnShowDlg: Button
    private lateinit var mTvMsg: TextView

    interface CustomDialogCallback {
        fun onCloseButtonClicked(inputText: String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mBtnShowDlg = findViewById(R.id.btnShowDlg)
        mTvMsg = findViewById(R.id.tvMsg)

        mBtnShowDlg.setOnClickListener() {

            _showCustomDialog(
                onCloseButtonClicked = {
                    Toast.makeText(this@MainActivity,"canceled ", Toast.LENGTH_SHORT).show()

                },
                onOkButtonClicked = { msg ->
                    mTvMsg.text = msg
                }
            )
        }
    }

    private fun _showCustomDialog(
        onCloseButtonClicked: () -> Unit,
        onOkButtonClicked: (String) -> Unit
    ) {
        val dlgView = LayoutInflater.from(this).inflate(R.layout.custom_dlg, null)

        val btnClose = dlgView.findViewById<Button>(R.id.btnClose)
        val btnOk = dlgView.findViewById<Button>(R.id.btnOk)
        val edMsg = dlgView.findViewById<EditText>(R.id.edMsg)

        val _dlg = AlertDialog.Builder(this).setView(dlgView).create()

        btnClose.setOnClickListener {

            _dlg.dismiss()

            onCloseButtonClicked()
        }

        btnOk.setOnClickListener {
//            Toast.makeText(this@MainActivity,"${edMsg.text}", Toast.LENGTH_SHORT).show()
            _dlg.dismiss()
            onOkButtonClicked(edMsg.text.toString())
        }

        _dlg.show()

    }
}