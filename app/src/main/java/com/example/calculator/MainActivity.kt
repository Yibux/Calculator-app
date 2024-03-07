package com.example.calculator

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val simpleCalculatorButton = findViewById<Button>(R.id.SimpleCalculatorButton)
        simpleCalculatorButton.setOnClickListener {
            startActivity(Intent(this, SimpleCalculator::class.java))
        }

        val advancedCalculatorButton = findViewById<Button>(R.id.AdvancedCalculatorButton)
        advancedCalculatorButton.setOnClickListener {
            startActivity(Intent(this, ScientificCalculator::class.java))
        }

    }
}
