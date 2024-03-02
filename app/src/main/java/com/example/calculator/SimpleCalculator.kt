package com.example.calculator

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

    fun equalsAction(view: View) {}

    fun insertNumberAction(view: View) {
        if (view is Button)
        {
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
}