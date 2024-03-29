package com.example.arkanoid

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*


class GameView(context: Context, attributeSet: AttributeSet)
    : SurfaceView(context, attributeSet), SurfaceHolder.Callback {

    private val thread : GameThread

    private var PREFS_NAME = "arkanoid"
    lateinit var sharedPref : SharedPreferences

    var myActivity : MainActivity? = null
    private var ball = Ball(0f,0f)
    private var paddle = Paddle(0f,0f)
    private var ballLaunched = false
    private var bricks :ArrayList<Brick> = ArrayList()
    private var points = 0
    private var lives = 3
    private var highScore = 0
    private var gameEnded = false



    init {
        holder.addCallback(this)
        thread = GameThread(holder, this)
    }


    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        thread.setRunning(false)
        thread.join()
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {

        paddle.x = width  / 2f
        paddle.y = height - 4*paddle.height
        ball.x = width  / 2f
        ball.y = paddle.y -paddle.height- ball.radius
        sharedPref = myActivity?.getSharedPreferences( PREFS_NAME, Context.MODE_PRIVATE)!!
        var bricksJson = sharedPref.getString("bricks", null)

        if(bricksJson != null){
            var bricksJsonArray = bricksJson.removePrefix("[").removeSuffix("]").split("}")
            for (b in bricksJsonArray){
                var b1 = b.removePrefix(",") + "}"
                if(b1.startsWith("{") && b1.endsWith("}")){
                    var gson = Gson()
                    var brick = gson.fromJson(b1.trim(),Brick::class.java)
                    bricks.add(brick)
                }

            }
        }
        else{
            generateBricks()
        }
        points = sharedPref.getInt("points",0)
        highScore = sharedPref.getInt("highScore",0)
        lives = sharedPref.getInt("lives",3)


        saveToPrefs()
        postLives(lives.toString())
        postPoints(points.toString())
        postHighScore(highScore.toString())
        thread.setRunning(true)
        thread.start()
    }

    fun generateBricks(){
        val rows = 6
        val cols = 6
        val space =(width - ((300f + (cols-1) * 100f))) /(cols-1)
        val brick = Brick(0f,0f)
        for( rc in 0 until rows){
            var y = rc * (brick.height*3) + 100f
            var x = 150f
            for (cc in 0 until cols){
                var b = Brick(x,y)
                bricks.add(b)
                x += ((2*brick.width) + space)
            }
        }
    }
    fun fullReset(){
        gameEnded = false
        paddle.x = width  / 2f
        paddle.y = height - 4*paddle.height
        ball.x = width  / 2f
        ball.y = paddle.y -paddle.height- ball.radius
        ballLaunched = false
        points = 0
        postPoints(points.toString())
        lives = 3
        postLives(lives.toString())
        bricks = ArrayList()
        generateBricks()
        saveToPrefs()
        postLives(lives.toString())
        postPoints(points.toString())
        postHighScore(highScore.toString())
        hideWinOrLose()
    }
    fun update() {

        ball.x+=ball.dx
        ball.y+=ball.dy
        var x=  paddle.x+paddle.dx
        if(x + paddle.width > width){
            paddle.x = width - paddle.width
        }
        else if(x - paddle.width < 0){
            paddle.x = paddle.width
        }
        else{
            paddle.x = x
            if(!ballLaunched){
                ball.x = x
            }
        }

        checkCollision()
        checkWin()



    }
    private fun checkWin(){

        if(bricks.isEmpty()){
            ball.dx = 0f
            ball.dy = 0f
            postWinOrLose("You Win :)")
            gameEnded = true
        }
    }
    private fun checkCollision(){
        if (ball.x - ball.radius <= 0 || ball.x+ball.radius >= width) {
            ball.dx = -ball.dx
        }
        if (ball.y - ball.radius <= 0 ) {
            ball.dy = -ball.dy
        }
        if( ball.y+ball.radius >= height) {
            reset()
            lives--
            postLives(lives.toString())
            if(lives <= 0){
                postWinOrLose("You Lost :(")
                gameEnded = true
            }
            return
        }



        val paddleTop = paddle.y - paddle.height
        val paddleBottom = paddle.y + paddle.height
        val paddleLeft = paddle.x - paddle.width
        val paddleRight = paddle.x + paddle.width



        if (ball.y + ball.radius >= paddleTop && ball.y - ball.radius <= paddleBottom
            && ball.x - ball.radius <= paddleRight && ball.x + ball.radius >= paddleLeft
        ) {
            val newBallMovementX = Math.abs(Math.abs(paddle.x) - Math.abs(ball.x)) / paddle.width

            if (ball.x <= paddle.x) {
                ball.dx = -1 * newBallMovementX * ball.radius
            } else {
                ball.dx = 1 * newBallMovementX * ball.radius
            }

            ball.dy = -ball.dy
        }



        for( brick in bricks){


            var brickTop = brick.y - brick.height - ball.radius
            var brickBottom = brick.y + brick.height + ball.radius
            var brickLeft = brick.x - brick.width - ball.radius
            var brickRight = brick.x + brick.width + ball.radius

            if((ball.y  >= brickTop) && (ball.y  <= brickBottom)
                && (ball.x  <= brickRight) && (ball.x  >= brickLeft)){

                bricks.remove(brick)

                if(ball.y  < brickTop) {
                    if(ball.x < brickLeft) {
                        // ball center at top left of brick
                        if(ball.dx > 0) {
                            ball.dx *= -1
                        } else {
                            ball.dy *= -1
                        }
                    } else if(ball.x > brickRight) {
                        // ball center at top right of brick
                        if(ball.dx < 0) {
                            ball.dx *= -1
                        } else {
                            ball.dy *= -1
                        }
                    } else {
                        // ball center at top center of brick
                        ball.dy *= -1
                    }
                } else if(ball.y > brickBottom) {
                    if(ball.x < brickLeft) {
                        // ball center at bottom left of brick
                        if(ball.dx > 0) {
                            ball.dx *= -1
                        } else {
                            ball.dy *= -1
                        }
                    } else if(ball.x > brickRight) {
                        // ball center at bottom right of brick
                        if(ball.dx < 0) {
                            ball.dx *= -1
                        } else {
                            ball.dy *= -1
                        }
                    } else {
                        // ball center at bottom center of brick
                        ball.dy *= -1
                    }
                } else {
                    if(ball.x < brickLeft) {
                        // ball center at middle left of brick
                        if(ball.dx > 0) {
                            ball.dx *= -1
                        } else {
                            ball.dy *= -1
                        }
                    } else if(ball.x > brickRight) {
                        // ball center at middle right of brick
                        if(ball.dx < 0) {
                            ball.dx *= -1
                        } else {
                            ball.dy *= -1
                        }
                    } else {
                        // ball center at middle of brick
                        // ball shouldn't come here. If it comes, it is a bug.
                        // reflect it vertically, just in case
                        ball.dy *= -1
                    }
                }

                points+= 10
                postPoints(points.toString())
                if(points > highScore){
                    highScore = points
                    postHighScore(highScore.toString())
                }

                saveToPrefs()


                return
            }
        }

    }


    private fun postPoints(msg :String){
        myActivity?.runOnUiThread{
            myActivity?.displayPoints(msg)
        }
    }
    private fun postHighScore(msg : String){
        myActivity?.runOnUiThread{
            myActivity?.displayHighScore(msg)
        }
    }
    private fun postLives(msg : String){
        myActivity?.runOnUiThread{
            myActivity?.displayLives(msg)
        }
    }
    private fun postWinOrLose(msg : String){
        myActivity?.runOnUiThread{
            myActivity?.displayWinOrLose(msg)
        }
    }
    private fun hideWinOrLose(){
        myActivity?.runOnUiThread{
            myActivity?.hideWinOrLose()
        }
    }

    private fun reset(){
        ballLaunched = false
        ball.dx = 0f
        ball.dy = 0f
        ball.x = paddle.x
        ball.y = paddle.y - paddle.height - ball.radius
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        if (canvas == null) return

        val red = Paint()
        red.setARGB(255,255,0,0)
        canvas.drawOval(RectF(ball.x-ball.radius, ball.y - ball.radius,ball.x+ball.radius,ball.y+ball.radius), red)

        val green = Paint()
        green.setARGB(255,0,255,0)
        canvas.drawRect(RectF(paddle.x-paddle.width, paddle.y - paddle.height,paddle.x+paddle.width,paddle.y+paddle.height), green)

        val blue = Paint()
        blue.setARGB(255,0,0,255)
        for(brick in bricks){
            canvas.drawRect(RectF(brick.x-brick.width, brick.y - brick.height,brick.x+brick.width,brick.y+brick.height), blue)
        }

    }
    private fun saveToPrefs(){
        val gson = Gson()
        val jsonString = gson.toJson(bricks)

        sharedPref = myActivity?.getSharedPreferences( PREFS_NAME, Context.MODE_PRIVATE)!!
        val editor : SharedPreferences.Editor = sharedPref.edit()
        editor.putString("bricks", jsonString)
        editor.putInt("points", points)
        editor.putInt("highScore", highScore)
        editor.putInt("lives", lives)
        editor!!.commit()
    }



    override fun onTouchEvent(event: MotionEvent): Boolean {
        if(!gameEnded){
            if(!ballLaunched && event.y < height/2){

                ballLaunched = !ballLaunched
                ball.dx = (if (Math.random() <= 0.5) -1 else 1) * ball.radius
                ball.dy = -1 * ball.radius
            }


            if(event.action == MotionEvent.ACTION_DOWN){
                if(event.y >= height/2){
                    if(event.x >= width/2 ){
                        paddle.dx = paddle.speed
                    }
                    else{
                        paddle.dx = -paddle.speed
                    }
                }
            }
            if(event.action == MotionEvent.ACTION_UP){
                paddle.dx = 0f
            }
        }






        return true

        return super.onTouchEvent(event)
    }
}