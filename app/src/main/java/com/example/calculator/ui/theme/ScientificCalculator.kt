package com.example.calculator.ui.theme

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.calculator.R
import java.lang.Math.log
import java.lang.Math.pow
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.log
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

class ScientificCalculator : AppCompatActivity() {

    private var isNumberMinus = false
    private var isAnyNumber = false
    private var takenOperation = ""
    private var isFirstNumberSelected = false
    private var isSecondNumberSelected = false
    private var firstNumber : Double = 0.0
    private var secondNumber : Double = 0.0
    private var output : Double = 0.0
    private var cleanerCounter = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_scientific_calculator)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun equalsAction(view: View) {
        if(isAnyNumber) {
            val numberString = findViewById<TextView>(R.id.outputView).text.toString()

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
                        findViewById<TextView>(R.id.outputView).text = "Error"
                        isFirstNumberSelected = false
                        isSecondNumberSelected = false
                        return
                    }
                    output = firstNumber / secondNumber
                }
                "log" -> output = log(firstNumber, secondNumber)
                "x^y" -> output = pow(firstNumber, secondNumber)
            }
            findViewById<TextView>(R.id.outputView).text = output.toString()
            isAnyNumber = false
            isNumberMinus = false
        }
    }

    fun insertNumberAction(view: View) {
        if (view is Button)
        {
            if(!isAnyNumber) {
                findViewById<TextView>(R.id.outputView).text = ""
            }

            if (view.text.equals(".") && findViewById<TextView>(R.id.outputView).text.isEmpty()) {
                findViewById<TextView>(R.id.outputView).append("0")
            }

            if(view.text.equals(".") &&
                findViewById<TextView>(R.id.outputView).text.count { it == '.' } == 1)
                return

            findViewById<TextView>(R.id.outputView).append(view.text)
            isAnyNumber = true
        }
    }

    fun deleteLastValue(view: View) {
        val textToEdit = findViewById<TextView>(R.id.outputView).text
        val textLength = textToEdit.length
        if (textToEdit.isNotBlank()) {
            if(isNumberMinus && textLength == 2) {
                clearAllOperations(view)
                return
            }

            findViewById<TextView>(R.id.outputView).text = textToEdit.subSequence(0, textLength - 1)

        }

        if (textLength == 0)
            isAnyNumber = false
    }

    //TODO: pierwsze klikniecie C czysci tylko wpisaną liczbę, podwójne wszystko
    fun clearAllOperations(view: View) {
        if (cleanerCounter == 0)
        {
            findViewById<TextView>(R.id.outputView).text = ""
            if(isSecondNumberSelected)
                isSecondNumberSelected = false
        }
        isAnyNumber = false
        isNumberMinus = false
        isFirstNumberSelected = false
    }
    fun changeNumberSymbol(view: View) {
        val textToEdit = findViewById<TextView>(R.id.outputView).text
        val textLength = textToEdit.length
        if(isAnyNumber) {
            if (isNumberMinus) {
                findViewById<TextView>(R.id.outputView).text = textToEdit.subSequence(1, textLength)
                isNumberMinus = false
            }
            else {
                findViewById<TextView>(R.id.outputView).text = "-$textToEdit"
                isNumberMinus = true
            }
        }
    }

    fun takeOperation(view: View) {
        if (view is Button) {
            if(isAnyNumber)
                calculateScientificActions(view.text.toString())

            if(isFirstNumberSelected && isAnyNumber) {
                equalsAction(view)
            }
            takenOperation = view.text.toString()
            val numberString = findViewById<TextView>(R.id.outputView).text.toString()

            firstNumber = if(numberString.endsWith("."))
                extractNumberFromString(numberString + "0")
            else
                extractNumberFromString(numberString)

            isFirstNumberSelected = true
            isAnyNumber = false
        }
    }

    private fun calculateScientificActions(text : String) {
        when(text) {
            "sin" -> output = sin(extractNumberFromString(findViewById<TextView>(R.id.outputView).text.toString()))
            "cos" -> output = cos(extractNumberFromString(findViewById<TextView>(R.id.outputView).text.toString()))
            "tan" -> output = tan(extractNumberFromString(findViewById<TextView>(R.id.outputView).text.toString()))
            "ln" -> output = ln(extractNumberFromString(findViewById<TextView>(R.id.outputView).text.toString()))
            "sqrt" -> output = sqrt(extractNumberFromString(findViewById<TextView>(R.id.outputView).text.toString()))
            "x^2" -> output =
                extractNumberFromString(findViewById<TextView>(R.id.outputView).text.toString()).pow(
                    2
                )
            else -> return
        }
        findViewById<TextView>(R.id.outputView).text = output.toString()
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