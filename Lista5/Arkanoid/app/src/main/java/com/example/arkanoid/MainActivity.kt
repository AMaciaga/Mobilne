package com.example.arkanoid

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gameView.myActivity = this
//        getSharedPreferences("arkanoid", 0).edit().clear().commit()
    }

    fun reset(view: View){
        gameView.fullReset()
    }
    fun displayPoints(msg : String){
        points.text = "points: $msg"
    }
    fun displayHighScore(msg : String){
        highScore.text = "highScore: $msg"
    }
    fun displayLives(msg: String){
        lives.text = "lives: $msg"
    }
    fun displayWinOrLose(msg: String){
        textView.text = msg
        textView.visibility = View.VISIBLE
    }
    fun hideWinOrLose(){
        textView.visibility = View.GONE
    }


}
