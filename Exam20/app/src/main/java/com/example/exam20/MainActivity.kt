package com.example.exam20

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var mbtnCalculate : Button
    private lateinit var mradioGroup : RadioGroup
    private lateinit var mtvResult : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mbtnCalculate = findViewById(R.id.btnCalculate)
        mradioGroup = findViewById(R.id.radioGroup)
        mtvResult = findViewById(R.id.tvResult)

        mbtnCalculate.setOnClickListener {
            val selectedId = mradioGroup.checkedRadioButtonId

            if (selectedId != -1) {
                val selectedRadioButton = findViewById<RadioButton>(selectedId)
                val operation = selectedRadioButton.text.toString()
                // Open custom dialog
                showCalculationDialog(operation,
                    onCalcBtn = { result ->
                        Toast.makeText(this, "Result: $result", Toast.LENGTH_SHORT).show()
                        mtvResult.text = result.toString()
                    }
                )
            }
            else {
                Toast.makeText(this, "Please select an operation", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun showCalculationDialog(operation: String,
                                      onCalcBtn : (Int) -> Unit
                                      ) {

        val dlgView = LayoutInflater.from(this).inflate(R.layout.dlg_cal, null)
        val _dlg = AlertDialog.Builder(this).setView(dlgView).create()

        val numA = dlgView.findViewById<EditText>(R.id.numA)
        val numB = dlgView.findViewById<EditText>(R.id.numB)
        val btnCalc = dlgView.findViewById<Button>(R.id.btnCalculate)

        btnCalc.setOnClickListener {
            val a = numA.text.toString().toInt()
            val b = numB.text.toString().toInt()

            if (a == null || b == null) {
                Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else {
                try {
                    val result = when (operation) {
                        "Addition (+)" -> a + b
                        "Subtraction (-)" -> a - b
                        "Multiplication (x)" -> a * b
                        "Division (÷)" -> a/b
                        else -> {
                            Toast.makeText(this, "Invalid operation", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }
                    }
                    if (result != null) {
                        onCalcBtn(result)
                        _dlg.dismiss()
                    }

                }
                catch (e : Exception) {
                    Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }


        }

        _dlg.show()



    }
}