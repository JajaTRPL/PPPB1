package com.example.kalkulator_pt2

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kalkulator_pt2.databinding.ActivityMainBinding
import java.util.*
import android.util.TypedValue

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var inputDisplay = "0"
    private var resultDisplay = 0
    private var inputTextSize = 64f

    private fun inputValue(value: String){
        val operator = arrayOf("*", "/", "+", "-")
        if(inputDisplay == "0"){
            if(!operator.contains(value)){
                inputDisplay = value
            }
        } else {
            if(operator.contains(inputDisplay.last().toString()) && operator.contains(value)){
                inputDisplay = inputDisplay.substring(0, inputDisplay.length -1) + value
            } else {
                inputDisplay += value
            }
        }
        binding.masukkanNilai.text = inputDisplay
    }

    private fun backspace(){
        if(inputDisplay.length > 1){
            inputDisplay = inputDisplay.substring(0, inputDisplay.length - 1)
        } else {
            if((inputDisplay.length == 1) && (inputDisplay !== "0")){
                inputDisplay = "0"
            }
        }
        binding.masukkanNilai.text = inputDisplay
    }

    private fun clearInput(){
        inputDisplay = "0"
        resultDisplay = 0
        binding.masukkanNilai.text = inputDisplay
        binding.hasilCalcu.text = resultDisplay.toString()
    }
    private fun calculate() {
        val operator = arrayOf("*", "/", "+", "-")
        if(operator.contains(inputDisplay.last().toString())){
            return
        }
        try {
            val numbers = Stack<Int>()
            val operators = Stack<Char>()

            var i = 0
            while (i < inputDisplay.length) {
                when {
                    inputDisplay[i].isDigit() -> {
                        var num = 0
                        while (i < inputDisplay.length && inputDisplay[i].isDigit()) {
                            num = num * 10 + (inputDisplay[i] - '0')
                            i++
                        }
                        numbers.push(num)
                        continue
                    }
                    inputDisplay[i] in "+-*/" -> {
                        while (operators.isNotEmpty() && precedence(operators.peek()) >= precedence(inputDisplay[i])) {
                            processOperation(numbers, operators)
                        }
                        operators.push(inputDisplay[i])
                    }
                }
                i++
            }

            while (operators.isNotEmpty()) {
                processOperation(numbers, operators)
            }

            resultDisplay = numbers.pop()
        } catch (e: Exception) {
            Toast.makeText(this, "Error in calculation", Toast.LENGTH_SHORT).show()
            resultDisplay = 0
        }

        inputDisplay = resultDisplay.toString()
        binding.masukkanNilai.text = inputDisplay
        binding.hasilCalcu.text = resultDisplay.toString()
    }

    private fun precedence(op: Char): Int {
        return when (op) {
            '+', '-' -> 1
            '*', '/' -> 2
            else -> 0
        }
    }

    private fun processOperation(numbers: Stack<Int>, operators: Stack<Char>) {
        val right = numbers.pop()
        val left = numbers.pop()
        val op = operators.pop()

        val result = when (op) {
            '+' -> left + right
            '-' -> left - right
            '*' -> left * right
            '/' -> left / right
            else -> 0
        }
        numbers.push(result)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            val buttons = mapOf(
                button0 to "0",
                button1 to "1",
                button2 to "2",
                button3 to "3",
                button4 to "4",
                button5 to "5",
                button6 to "6",
                button7 to "7",
                button8 to "8",
                button9 to "9",
                buttonbagi to "/",
                buttonkali to "*",
                buttontambah to "+",
                buttonkurang to "-"
            )
            masukkanNilai.text = inputDisplay
            hasilCalcu.text = resultDisplay.toString()

            buttons.forEach { button, value ->
                button.setOnClickListener {
                    inputValue(value)

                    if((inputDisplay.length > 10) && (inputTextSize >= 32f)){
                        inputTextSize -= 2f
                        masukkanNilai.setTextSize(TypedValue.COMPLEX_UNIT_SP, inputTextSize)
                    }
                }
            }
            hapusAngka.setOnClickListener {
                backspace()

                if((inputDisplay.length > 10) && (inputTextSize <= 64f)){
                    inputTextSize += 2f
                    masukkanNilai.setTextSize(TypedValue.COMPLEX_UNIT_SP, inputTextSize)
                }
            }
            buttonClear.setOnClickListener {
                clearInput()
            }
            buttonHitung.setOnClickListener {
                calculate()
                if((inputDisplay.length > 10) && (inputTextSize <= 64f)){
                    val newInputTextSize = (inputDisplay.length - 10) * 2f
                    inputTextSize = if((newInputTextSize >= 32f ) && (newInputTextSize <= 64f )) newInputTextSize else inputTextSize
                    masukkanNilai.setTextSize(TypedValue.COMPLEX_UNIT_SP, inputTextSize)
                } else {
                    inputTextSize = 64f
                    masukkanNilai.setTextSize(TypedValue.COMPLEX_UNIT_SP, inputTextSize)
                }
                Toast.makeText(this@MainActivity, resultDisplay.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
}