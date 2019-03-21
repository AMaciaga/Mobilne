package com.example.memory

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.util.*


class MainActivity : AppCompatActivity() {

    var numbers: ArrayList<Int> = ArrayList()
    var fields: IntArray = intArrayOf(-1,-1,-1,-1)
    var guessedFields = 0
    var points = 0
    var firstClick = true
    var firstButton = -1
    var secondButton = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        generate();
        findViewById<TextView>(R.id.textView2).text = "Points: $points"

    }
    fun generate(){
        guessedFields = 0
        for(i in 1..2){
            numbers.add(i)
            numbers.add(i)
        }
        for(i in 0 until fields.size){
            if(numbers.isEmpty()){
                Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show()
            }
            var x = numbers.random()
            fields[i]=x
            numbers.remove(x)
        }
    }
    fun refresh(view: View){
        hideAll()
        generate()
    }
    fun resolve(){
        if(firstClick){
            show(firstButton)
            firstClick = false
        }
        else{
            show(secondButton)
            if(fields[firstButton-1] != fields[secondButton-1]){
                Handler().postDelayed({
                    hide(firstButton)
                    hide(secondButton)},500)
            }
            else{
                guessedFields++
                findButton(firstButton).setTextAppearance(R.style.buttonCorrect)
                findButton(secondButton).setTextAppearance(R.style.buttonCorrect)
                if(guessedFields ==2){
                    points++
                    findViewById<TextView>(R.id.textView2).text = "Points: $points"
                    Toast.makeText(this, "You won!", Toast.LENGTH_SHORT).show()
                }
            }
            firstClick = true
        }
    }
    fun findButton(nr: Int): Button{
        var button = findViewById<Button>(R.id.button1)
        when(nr){
            2-> button = findViewById<Button>(R.id.button2)
            3-> button = findViewById<Button>(R.id.button3)
            4-> button = findViewById<Button>(R.id.button4)
            else ->{}
        }
        return button
    }
    fun show(nr: Int){
        var button = findButton(nr)
        button.text = ""+fields[nr-1]
        button.isClickable = false;
    }
    fun hide(b1: Int){
        var button = findButton(b1)
        button.text = ""
        button.isClickable = true;
    }
    fun hideAll(){
        for(i in 1..4){
            hide(i)
            var b = findButton(i)
            b.setTextAppearance(R.style.button)
        }
    }
    fun button1(view: View) {
        if(firstClick){
            firstButton = 1
        }
        else{
            secondButton = 1
        }
        resolve()
    }
    fun button2(view: View) {
        if(firstClick){
            firstButton = 2
        }
        else{
            secondButton = 2
        }
        resolve()
    }
    fun button3(view: View) {
        if(firstClick){
            firstButton = 3
        }
        else{
            secondButton = 3
        }
        resolve()
    }
    fun button4(view: View) {
        if(firstClick){
            firstButton = 4
        }
        else{
            secondButton = 4
        }
        resolve()
    }
}
