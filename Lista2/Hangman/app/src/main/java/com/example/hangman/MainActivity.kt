package com.example.hangman

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var letter : String = ""
    private var dictionary : Array<String> = arrayOf()
    var word : String = ""
    var wordPlaceholder : String = ""
    var imgNr = 0
    val alphabet = "abcdefghijklmnoprstuwyz"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dictionary = resources.getStringArray(R.array.words)
        word = dictionary.random()
        wordPlaceholder = ""
        for(i in 1 until word.length){
            wordPlaceholder += "_ "
        }
        wordPlaceholder += "_"
        textView.text = wordPlaceholder

    }
    fun validate(){
        disableButton()
        if(!word.contains(letter)){
            imgNr++
            changeImg()
            if(imgNr == 11){
                Toast.makeText(this, "You lost :(", Toast.LENGTH_SHORT).show()
                disableAll()
            }

        }
        else{
            for(i in 0 until word.length){
                if(letter[0] == word[i]){
                    wordPlaceholder = wordPlaceholder.replaceRange(2*i,2*i+1,letter)
                }
            }
            textView.text = wordPlaceholder
            if(!wordPlaceholder.contains("_")){
                Toast.makeText(this, "You won :)", Toast.LENGTH_SHORT).show()
                disableAll()
            }
        }
    }
    private fun changeImg(){
        val id = if(imgNr < 10){
            resources.getIdentifier("wisielec0$imgNr","drawable",this.packageName)
        } else{
            resources.getIdentifier("wisielec$imgNr","drawable",this.packageName)
        }
        imageView.setImageResource(id)
    }
    private fun disableAll(){
        for(i in 0 until alphabet.length){
            letter = ""+alphabet[i]
            disableButton()
        }
    }
    private fun enableAll(){
        for(i in 0 until alphabet.length){
            letter = ""+alphabet[i]
            enableButton()
        }
    }
    private fun disableButton(){
        val id = resources.getIdentifier("$letter","id",this.packageName)
        val btn = findViewById<Button>(id)
        btn.isClickable = false
        btn.setTextAppearance(R.style.disabledButton)
    }
    fun enableButton(){
        val id = resources.getIdentifier("$letter","id",this.packageName)
        val btn = findViewById<Button>(id)
        btn.isClickable = true
        btn.setTextAppearance(R.style.button)
    }
    fun refresh(view: View){
        word = dictionary.random()
        wordPlaceholder = ""
        for(i in 1 until word.length){
            wordPlaceholder += "_ "
        }
        wordPlaceholder += "_"
        textView.text = wordPlaceholder
        enableAll()
        imgNr = 0
        changeImg()
    }
    fun onClick(view: View){
        val btn = view as Button
        letter = btn.text.toString()
        validate()
    }

}
