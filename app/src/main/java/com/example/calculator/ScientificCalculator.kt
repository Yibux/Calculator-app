package com.example.calculator
import android.icu.text.DecimalFormat
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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
    private lateinit var outputText : TextView
    private lateinit var clearButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val layoutId = intent.getIntExtra("layout", R.layout.activity_scientific_calculator)
        setContentView(layoutId)
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

    fun equalsAction(view: View) {
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
                "log" -> output = log(firstNumber, secondNumber)
                "x^y" -> output = firstNumber.pow(secondNumber)
                else -> return
            }

            displayOutput()

            clearLine = true
            isNumberMinus = if(output < 0) true else false
            isSecondNumberSelected = false
            isFirstNumberSelected = if(view is Button) !view.text.equals("=") else false
        }
    }

    private fun displayOutput() {
        outputText.text = if (output % 1 == 0.0) String.format("%.0f", output) else DecimalFormat("0.###").format(output)
    }


    fun insertNumberAction(view: View) {
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
            if(!outputText.text.isEmpty()
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

    fun deleteLastValue(view: View) {
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

    fun clearAllOperations(view: View) {
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
    fun changeNumberSymbol(view: View) {
        val textToEdit = outputText.text
        val textLength = textToEdit.length
        if(isAnyNumber) {
            if (isNumberMinus) {
                outputText.text = textToEdit.subSequence(1, textLength)
                isNumberMinus = false
            }
            else {
                outputText.text = "-$textToEdit"
                isNumberMinus = true

            }
        }
    }

    fun takeOperation(view: View) {
        if (view is Button && isAnyNumber) {

            equalsAction(view)

            takenOperation = view.text.toString()

            calculateScientificActions(takenOperation)

            val numberString = outputText.text.toString()

            firstNumber = if(numberString.endsWith("."))
                extractNumberFromString(numberString + "0")
            else
                extractNumberFromString(numberString)

            clearLine = true
            isFirstNumberSelected = true
        }
    }

    private fun calculateScientificActions(text : String) {
        when(text) {
            "sin" -> output = sin(extractNumberFromString(outputText.text.toString()))
            "cos" -> output = cos(extractNumberFromString(outputText.text.toString()))
            "tan" -> output = tan(extractNumberFromString(outputText.text.toString()))
            "ln" -> output = ln(extractNumberFromString(outputText.text.toString()))
            "sqrt" -> output = sqrt(extractNumberFromString(outputText.text.toString()))
            "%" -> output = extractNumberFromString(outputText.text.toString()) / 100
            "x^2" -> output =
                extractNumberFromString(outputText.text.toString()).pow(
                    2
                )
            else -> return
        }
        isNumberMinus = if(output < 0) true else false
        displayOutput()
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
