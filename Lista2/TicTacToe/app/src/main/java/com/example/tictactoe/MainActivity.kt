package com.example.tictactoe

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TableRow
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var isPvE = false
    var isOTurn = true
    var gameEnded = false
    var turn = "O turn"
    var pressedButton : Button? = null
    var row = -1
    var col = -1
    var fields = arrayOf<Array<Int>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        for (i in 0..4) {
            var array = arrayOf<Int>()
            for (j in 0..4) {
                array += -1
            }
            fields += array
        }
        textView.text = turn
    }

    override fun onResume() {
        super.onResume()
        if(isPvE && !isOTurn && !gameEnded) makeMove()
    }
    fun endGame(){
        var msg = "won"
        if(isOTurn){
            msg = "X $msg"
        }
        else{
            msg = "0 $msg"
        }
        textView.text = msg
        disableAll()
        gameEnded = true
    }
    fun makeMove(){
        val move = findMove()
        row = move[0]
        col = move[1]
        fields[row][col] = 1
        val id = resources.getIdentifier("button$row$col","id",this.packageName)
        val btn = findViewById<Button>(id)
        btn.text = "X"
        btn.isClickable = false
        isOTurn=true
        turn = "O turn"
        textView.text = turn
        validate()
    }
    fun findMove(): Array<Int>{
        var possibleMoves = arrayOf<Array<Int>>()
        var possibleBlocks = arrayOf<Array<Int>>()

        for (i in 0..4) {
            for (j in 0..4) {
                if(fields[i][j] == -1){
                    var xCount = 0
                    var oCount = 0
                    for(k in 0..4){
                        if(fields[i][k] == 0) oCount++
                        if(fields[i][k] == 1) xCount++
                    }
                    if(oCount == 0){
                        val array = arrayOf(i,j,xCount)
                        possibleMoves += array
                    }
                    if(xCount == 0){
                        val array = arrayOf(i,j,oCount)
                        possibleBlocks += array
                    }
                    xCount = 0
                    oCount = 0
                    for(k in 0..4){
                        if(fields[k][j] == 0) oCount++
                        if(fields[k][j] == 1) xCount++
                    }
                    if(oCount == 0){
                        val array = arrayOf(i,j,xCount)
                        possibleMoves += array
                    }
                    if(xCount == 0){
                        val array = arrayOf(i,j,oCount)
                        possibleBlocks += array
                    }
                    if(i==j){
                        xCount = 0
                        oCount = 0
                        for(k in 0..4){
                            if(fields[k][k] == 0) oCount++
                            if(fields[k][k] == 1) xCount++
                        }
                        if(oCount == 0){
                            val array = arrayOf(i,j,xCount)
                            possibleMoves += array
                        }
                        if(xCount == 0){
                            val array = arrayOf(i,j,oCount)
                            possibleBlocks += array
                        }
                    }
                    if(i==4-j){
                        xCount = 0
                        oCount = 0
                        for(k in 0..4){
                            if(fields[k][4-k] == 0) oCount++
                            if(fields[k][4-k] == 1) xCount++
                        }
                        if(oCount == 0){
                            val array = arrayOf(i,j,xCount)
                            possibleMoves += array
                        }
                        if(xCount == 0){
                            val array = arrayOf(i,j,oCount)
                            possibleBlocks += array
                        }
                    }
                }
            }
        }
        if(!possibleMoves.isEmpty() || !possibleBlocks.isEmpty()){
            var optimalMoves = arrayOf<Array<Int>>()
            var max = 0
            for(i in 0 until possibleMoves.size){
                if(possibleMoves[i][2] > max){
                    max = possibleMoves[i][2]
                    optimalMoves = arrayOf()
                    optimalMoves += possibleMoves[i]
                }
                else if(possibleMoves[i][2] == max){
                    optimalMoves += possibleMoves[i]
                }
            }
            var optimalBlocks = arrayOf<Array<Int>>()
            max = 0
            for(i in 0 until possibleBlocks.size){
                if(possibleBlocks[i][2] > max){
                    max = possibleBlocks[i][2]
                    optimalBlocks = arrayOf()
                    optimalBlocks += possibleBlocks[i]
                }
                else if(possibleBlocks[i][2] == max){
                    optimalBlocks += possibleBlocks[i]
                }
            }
            if(optimalBlocks.isEmpty()){
                return optimalMoves.random()
            }
            if(optimalMoves.isEmpty()){
                return optimalBlocks.random()
            }
            if(optimalBlocks[0][2]>optimalMoves[0][2]){
                return optimalBlocks.random()
            }
            else{
                return optimalMoves.random()
            }
        }
        else{
            for (i in 0..4) {
                for (j in 0..4) {
                    if(fields[i][j] == -1){
                        val array = arrayOf(i,j)
                        possibleMoves += array
                    }
                }
            }
            return possibleMoves.random()
        }
    }
    fun playerAction(){
        pressedButton?.isClickable=false
        if(isOTurn){
            pressedButton?.text = "0"
            fields[row][col]=0
            isOTurn=false
            turn = "X turn"
        }
        else{
            pressedButton?.text = "X"
            fields[row][col]=1
            isOTurn=true
            turn = "O turn"
        }
        textView.text = turn
        validate()
        if(isPvE && !isOTurn && !gameEnded) makeMove()
    }
    fun validate(){

        var isFinished = true
        for(i in 0..4){
            if(fields[row][i] != fields[row][col]){
                isFinished = false
            }
        }
        if(isFinished){
            endGame()
            return
        }
        isFinished = true
        for(i in 0..4){
            if(fields[i][col] != fields[row][col]){
                isFinished = false
            }
        }
        if(isFinished){
            endGame()
            return
        }
        if(row == col ){
            isFinished = true
            for(i in 0..4){
                if(fields[i][i] != fields[row][col]){
                    isFinished = false
                }
            }
            if(isFinished){
                endGame()
                return
            }
        }
        if(row == 4-col){
            isFinished = true
            for(i in 0..4){
                if(fields[i][4-i] != fields[row][col]){
                    isFinished = false
                }
            }
            if(isFinished){
                endGame()
                return
            }
        }
        fields.forEach { array -> if(array.contains(-1)) return }
        var msg = "Draw"
        textView.text = msg
        disableAll()
        gameEnded = true
    }

    fun disableAll(){

        var count = table.childCount
        for(i in 0 until count){
            var v : TableRow =  table.getChildAt(i) as TableRow
            var deadWeight = v.childCount
            for(j in 0 until deadWeight){
                var btn : Button = v.getChildAt(j) as Button
                btn.isClickable = false
            }
        }
    }
    fun enableAll(){

        var count = table.childCount
        for(i in 0 until count){
            var v : TableRow =  table.getChildAt(i) as TableRow
            var deadWeight = v.childCount
            for(j in 0 until deadWeight){
                var btn : Button = v.getChildAt(j) as Button
                btn.isClickable = true
                btn.text = ""
            }
        }
    }
    fun restart(){
        enableAll()
        textView.text = turn
        for (i in 0..4) {
            for (j in 0..4) {
                fields[i][j] = -1
            }
        }
        gameEnded = false
        if(isPvE){
            if(!isOTurn){
                makeMove()
            }
        }
    }
    fun refresh(view: View){
        restart()
    }
    fun changeMode(view: View){
        restart()
        isPvE = !isPvE
        if(isPvE){
            changeMode.text = resources.getString(R.string.pvp)
            if(!isOTurn){
                makeMove()
            }
        }
        else{
            changeMode.text = resources.getString(R.string.pve)
        }
    }
    fun onClick(view: View){
        pressedButton = view as Button?
        val tag = pressedButton?.tag as String
        row = tag.substring(0,1).toInt()
        col = tag.substring(1,2).toInt()
        playerAction()
    }

}
