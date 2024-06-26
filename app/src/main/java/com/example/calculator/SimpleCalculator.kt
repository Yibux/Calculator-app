package com.example.calculator

import android.icu.text.DecimalFormat
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.log
import kotlin.math.pow

open class SimpleCalculator : AppCompatActivity() {
    protected var clearLine = false
    protected var isNumberMinus = false
    protected var isAnyNumber = false
    protected var takenOperation = ""
    protected var isFirstNumberSelected = false
    protected var isSecondNumberSelected = false
    protected var firstNumber : Double = 0.0
    protected var secondNumber : Double = 0.0
    protected var output : Double = 0.0
    protected var cleanerCounter = 0
    protected lateinit var outputText : TextView
    protected lateinit var clearButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_simple_calculator)
        findViewById<Button>(R.id.clearButton).text = "AC"

        outputText = findViewById<TextView>(R.id.outputView)
        clearButton = findViewById<Button>(R.id.clearButton)

        if(savedInstanceState != null) {
            findViewById<TextView>(R.id.outputView).text = savedInstanceState.getString("outputViewText")
            this.clearLine = savedInstanceState.getBoolean("clearLine")
            this.isNumberMinus = savedInstanceState.getBoolean("isNumberMinus")
            this.isAnyNumber = savedInstanceState.getBoolean("isAnyNumber")
            this.takenOperation = savedInstanceState.getString("takenOperation").toString()
            this.isFirstNumberSelected = savedInstanceState.getBoolean("isFirstNumberSelected")
            this.isSecondNumberSelected = savedInstanceState.getBoolean("isSecondNumberSelected")
            this.firstNumber = savedInstanceState.getDouble("firstNumber")
            this.secondNumber = savedInstanceState.getDouble("secondNumber")
            this.output = savedInstanceState.getDouble("output")
            this.cleanerCounter = savedInstanceState.getInt("cleanerCounter")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val outputViewText = findViewById<TextView>(R.id.outputView).text.toString()
        outState.putString("outputViewText", outputViewText)
        outState.putBoolean("clearLine", clearLine)
        outState.putBoolean("isNumberMinus", isNumberMinus)
        outState.putBoolean("isAnyNumber", isAnyNumber)
        outState.putString("takenOperation", takenOperation)
        outState.putBoolean("isFirstNumberSelected", isFirstNumberSelected)
        outState.putBoolean("isSecondNumberSelected", isSecondNumberSelected)
        outState.putDouble("firstNumber", firstNumber)
        outState.putDouble("secondNumber", secondNumber)
        outState.putDouble("output", output)
        outState.putInt("cleanerCounter", cleanerCounter)
    }

    open fun equalsAction(view: View) {
        if (outputText.text.isEmpty()) {
            Toast.makeText(this, "Illegal operation", Toast.LENGTH_SHORT).show()
            return
        }

        if(isFirstNumberSelected && isAnyNumber && !clearLine) {
            val numberString = outputText.text.toString()

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
                        outputText.text = "Error"
                        isFirstNumberSelected = false
                        isSecondNumberSelected = false
                        clearLine = true
                        isNumberMinus = false
                        return
                    }
                    output = firstNumber / secondNumber
                }
                else -> return
            }

            displayOutput()

            clearLine = true
            isNumberMinus = if(output < 0) true else false
            isSecondNumberSelected = false
            isFirstNumberSelected = if(view is Button) !view.text.equals("=") else false
        }
    }

    protected fun displayOutput() {
        outputText.text = if (output % 1 == 0.0) String.format("%.0f", output) else DecimalFormat("0.###").format(output)
    }


    open fun insertNumberAction(view: View) {
        if (view is Button)
        {
            if(clearLine) {
                outputText.setText("")
                isAnyNumber = false
                clearLine = false
                cleanerCounter = 0
                clearButton.setText("AC")
            }

            if (view.text.equals(".") && outputText.text.isEmpty()) {
                outputText.append("0")
            }

            if(view.text.equals(".") &&
                outputText.text.count { it == '.' } == 1)
                return
            if(outputText.text.isNotEmpty()
                && extractNumberFromString(outputText.text.toString()) == 0.toDouble()
                && !view.text.equals(".")
                && outputText.text.count { it == '.' } == 0)
            {
                outputText.setText("")
            }

            outputText.append(view.text)
            isAnyNumber = true
            clearButton.setText("C")
        }
    }

    open fun deleteLastValue(view: View) {
        val textToEdit = outputText.text
        val textLength = textToEdit.length
        if (textToEdit.isNotBlank()) {
            if(isNumberMinus && textLength == 2) {
                cleanerCounter = 0
                clearAllOperations(view)
                return
            }

            outputText.text = textToEdit.subSequence(0, textLength - 1)

        }

        if (textLength == 0)
            clearAllOperations(view)
    }

    open fun clearAllOperations(view: View) {
        if (cleanerCounter == 0) {
            cleanerCounter++
        } else {
            isFirstNumberSelected = false
            isSecondNumberSelected = false
            cleanerCounter = 0
            takenOperation = ""
        }
        clearButton.setText("AC")
        outputText.text = ""
        isNumberMinus = false
        isAnyNumber = false
        clearLine = false
    }
    open fun changeNumberSymbol(view: View) {
        val textToEdit = outputText.text
        val textLength = textToEdit.length
        if(isAnyNumber) {
            if (outputText.text.startsWith("-")) {
                outputText.text = textToEdit.subSequence(1, textLength)
                isNumberMinus = false
            }
            else {
                outputText.text = "-$textToEdit"
                isNumberMinus = true

            }
        }
    }

    open fun takeOperation(view: View) {
        if (view is Button && isAnyNumber) {

            equalsAction(view)

            takenOperation = view.text.toString()

            val numberString = outputText.text.toString()

            firstNumber = if(numberString.endsWith("."))
                extractNumberFromString(numberString + "0")
            else
                extractNumberFromString(numberString)

            clearLine = true
            isFirstNumberSelected = true
        }
    }

    protected fun extractNumberFromString(numberString : String) : Double {
        isNumberMinus = false
        return if (numberString.startsWith("-")) {
            numberString.substring(1).toDouble() * (-1)
        } else {
            numberString.toDouble()
        }
    }
}