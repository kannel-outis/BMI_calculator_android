package com.example.bmicalculator

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.TextView.VISIBLE
import android.widget.TextView.INVISIBLE
import android.widget.Toast
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var resultText: TextView
    private lateinit var resultTextMedium: TextView
    private lateinit var resultTextSmall: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreferencesEditor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        resultText = findViewById(R.id.result_text)
        resultTextMedium = findViewById(R.id.result_text_medium)
        resultTextSmall = findViewById(R.id.result_text_small)
        resultTextMedium.visibility = INVISIBLE
        resultText.text = 0.0.toString()
        sharedPreferences = getSharedPreferences("BMI.calc", MODE_PRIVATE)
        sharedPreferencesEditor = sharedPreferences.edit()
        val lastCheckResult = sharedPreferences.getString("last_bmi", null)
        if(lastCheckResult != null){
            resultText.text = lastCheckResult
            resultTextMedium.text = sharedPreferences.getString("last_bmi_label", null)
            resultTextMedium.visibility = VISIBLE
            Toast.makeText(this, "Result from last session", Toast.LENGTH_LONG).show()
        }
        val weightTextField = findViewById<EditText>(R.id.cv_textfield)
        val heightTextField = findViewById<EditText>(R.id.tv_textfield)
        val calculate = findViewById<Button>(R.id.calculate_button)
        calculate.setOnClickListener {
            val weight = weightTextField.text.toString()
            val height = heightTextField.text.toString()

            if(validateInputs(height, weight)){
                val heightInMeters = height.toFloat()/100
                val bmi = weight.toFloat() / (heightInMeters * heightInMeters)
                val bmiResult = String.format("%.2f", bmi).toFloat()
                displayResult(bmiResult)
            }

        }


    }

    private fun validateInputs(height: String?, weight: String?): Boolean{
        if(height.isNullOrEmpty() || weight.isNullOrEmpty()){
            val toast = Toast.makeText(this, "No field must be empty", Toast.LENGTH_LONG)
            toast.show()
            return false
        }
        return true
    }
   private fun displayResult( result: Float){
        var color: Int = 0;
        if(result > 24.9){
            resultTextMedium.text = "Bmi is over the normal"
            color = ContextCompat.getColor(this, R.color.text_danger)
        }else if(result < 18.5){
            resultTextMedium.text = "Bmi is below the normal"
            color = ContextCompat.getColor(this,R.color.text_hint)
        }else{
            resultTextMedium.text = "You are healthy"
             color = ContextCompat.getColor(this,R.color.teal_700)
        }
       resultTextMedium.visibility = VISIBLE
       resultTextMedium.setTextColor(color)
       resultText.text = result.toString()

       sharedPreferencesEditor.apply {
           putString("last_bmi", result.toString())
           putString("last_bmi_label", resultTextMedium.text.toString())
           commit()
       }


   }

}