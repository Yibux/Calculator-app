package com.example.calculator

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.lang.Math.pow
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.log
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

class ScientificCalculator : AppCompatActivity() {

    private var clearLine = false
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

        val layoutId = intent.getIntExtra("layout", R.layout.activity_scientific_calculator)
        setContentView(layoutId)
    }

    fun equalsAction(view: View) {

        val outputView = findViewById<TextView>(R.id.outputView)
        if (outputView.text.isEmpty()) {
            Toast.makeText(this, "Illegal operation", Toast.LENGTH_SHORT).show()
            return
        }

        if(calculateScientificActions(takenOperation))
            return

        if(isAnyNumber && !clearLine) {
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
                "x^y" -> output = firstNumber.pow(secondNumber)
                else -> return
            }
            findViewById<TextView>(R.id.outputView).text = output.toString()
            clearLine = true
            isNumberMinus = if(output < 0) true else false
            isSecondNumberSelected = false
            isFirstNumberSelected = if(view is Button) !view.text.equals("=") else false
        }
    }
//        else {
//            Toast.makeText(this, "Insert second number", Toast.LENGTH_SHORT).show()
//        }

    fun insertNumberAction(view: View) {
        if (view is Button)
        {
            if(clearLine) {
                findViewById<TextView>(R.id.outputView).text = ""
                isAnyNumber = false
                clearLine = false
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
            //todo: check if it's necessary, fix this issue - after clicking C, the first number is erased (but shouldnt)
            if(isSecondNumberSelected)
                isSecondNumberSelected = false
            else
                isFirstNumberSelected = false
            isAnyNumber = false
            cleanerCounter++
        }
        else
        {
            findViewById<TextView>(R.id.outputView).text = ""
            isFirstNumberSelected = false
            isSecondNumberSelected = false
            isAnyNumber = false
            isNumberMinus = false
            cleanerCounter = 0
        }
        clearLine = false
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
        if (view is Button && isAnyNumber) {

            takenOperation = view.text.toString()
            equalsAction(view)

            val numberString = findViewById<TextView>(R.id.outputView).text.toString()

            firstNumber = if(numberString.endsWith("."))
                extractNumberFromString(numberString + "0")
            else
                extractNumberFromString(numberString)

            isFirstNumberSelected = true
            clearLine = true
        }
    }

    private fun calculateScientificActions(text : String) : Boolean {
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
            else -> return false
        }
        findViewById<TextView>(R.id.outputView).text = output.toString()
        return true
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