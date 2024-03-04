package com.example.calculator

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.calculator.R.id.outputView

class SimpleCalculator : ComponentActivity() {
    private var isNumberMinus = false
    private var isAnyNumber = false
    private var takenOperation = ""
    private var isFirstNumberSelected = false
    private var isSecondNumberSelected = false
    private var firstNumber : Double = 0.0
    private var secondNumber : Double = 0.0
    private var output : Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_simple_calculator)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun equalsAction(view: View) {
        if(isAnyNumber) {
            val numberString = findViewById<TextView>(outputView).text.toString()

            secondNumber = if(numberString.endsWith("."))
                extractNumberFromString(numberString + "0")
            else
                extractNumberFromString(numberString)

            isSecondNumberSelected = true
        }


        if(isFirstNumberSelected && isSecondNumberSelected) {
            when(takenOperation) {
                "+" -> output = firstNumber + secondNumber
                "-" -> output = firstNumber - secondNumber
                "*" -> output = firstNumber * secondNumber
                "/" -> {
                    if(secondNumber.equals(0.0)) {
                        findViewById<TextView>(outputView).text = "Error"
                        isFirstNumberSelected = false
                        isSecondNumberSelected = false
                        return
                    }
                    output = firstNumber / secondNumber
                }
            }
            findViewById<TextView>(outputView).text = output.toString()
            isAnyNumber = false
        }
    }

    fun insertNumberAction(view: View) {
        if (view is Button)
        {
            if(!isAnyNumber) {
                findViewById<TextView>(outputView).text = ""
            }

            if (view.text.equals(".") && findViewById<TextView>(outputView).text.isEmpty()) {
                findViewById<TextView>(outputView).append("0")
            }

            if(view.text.equals(".") &&
                findViewById<TextView>(outputView).text.count { it == '.' } == 1)
                return

            findViewById<TextView>(outputView).append(view.text)
            isAnyNumber = true
        }
    }

    fun deleteLastValue(view: View) {
        val textToEdit = findViewById<TextView>(outputView).text
        val textLength = textToEdit.length
        if (textToEdit.isNotBlank()) {
            if(isNumberMinus && textLength == 2) {
                clearAllOperations(view)
                return
            }

            findViewById<TextView>(outputView).text = textToEdit.subSequence(0, textLength - 1)

        }

        if (textLength == 0)
            isAnyNumber = false
    }
    fun clearAllOperations(view: View) {
        findViewById<TextView>(outputView).text = ""
        isAnyNumber = false
        isNumberMinus = false
        isFirstNumberSelected = false
    }
    fun changeNumberSymbol(view: View) {
        val textToEdit = findViewById<TextView>(outputView).text
        val textLength = textToEdit.length
        if(isAnyNumber) {
            if (isNumberMinus) {
                findViewById<TextView>(outputView).text = textToEdit.subSequence(1, textLength)
                isNumberMinus = false
            }
            else {
                findViewById<TextView>(outputView).text = "-$textToEdit"
                isNumberMinus = true
            }
        }
    }

    fun takeOperation(view: View) {
        if (view is Button) {
            if(isFirstNumberSelected && isAnyNumber) {
                equalsAction(view)
            }
            takenOperation = view.text.toString()
            val numberString = findViewById<TextView>(outputView).text.toString()

            firstNumber = if(numberString.endsWith("."))
                extractNumberFromString(numberString + "0")
            else
                extractNumberFromString(numberString)

            isFirstNumberSelected = true
            isAnyNumber = false
        }
    }

    private fun extractNumberFromString(numberString : String) : Double {
        isNumberMinus = false
        return if (numberString.startsWith("-")) {
            numberString.substring(1).toDouble() * (-1)
        } else {
            numberString.toDouble()
        }
    }


}