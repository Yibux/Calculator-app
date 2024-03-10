package com.example.calculator

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val simpleCalculatorButton = findViewById<Button>(R.id.SimpleCalculatorButton)
        val advancedCalculatorButton = findViewById<Button>(R.id.AdvancedCalculatorButton)

        val clickListener = View.OnClickListener { view ->
            val intent = Intent(this, ScientificCalculator::class.java)
            when (view.id) {
                R.id.SimpleCalculatorButton -> intent.putExtra("layout", R.layout.activity_simple_calculator)
                R.id.AdvancedCalculatorButton -> intent.putExtra("layout", R.layout.activity_scientific_calculator)
            }
            startActivity(intent)
        }

        simpleCalculatorButton.setOnClickListener(clickListener)
        advancedCalculatorButton.setOnClickListener(clickListener)
        val aboutButton = findViewById<Button>(R.id.AboutButton)
        aboutButton.setOnClickListener {
            startActivity(Intent(this, About::class.java))
        }

        findViewById<Button>(R.id.QuitButton).setOnClickListener {
            finish()
        }
    }
}
