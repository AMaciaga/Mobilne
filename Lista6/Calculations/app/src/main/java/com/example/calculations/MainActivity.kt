package com.example.calculations

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var retrofit: Retrofit
    private lateinit var newton : NewtonAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        retrofit = Retrofit.Builder()
            .baseUrl("https://newton.now.sh")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        newton = retrofit.create(NewtonAPI::class.java)
    }
    fun onClick(view: View){
        if(task.text.isNotBlank()){
            val button = view as Button
            val operation = button.text.toString()
            val calculation = task.text.toString()
            var expression = calculation.replace("/","(over)")
            if((operation == "tangent"  || operation == "log")){
                if(firstArg.text.isBlank()){
                    return
                }
                expression = firstArg.text.toString()+"|"+expression
            }
            if(operation == "area"){
                if(firstArg.text.isBlank() || secondArg.text.isBlank() ){
                    return
                }
                expression = firstArg.text.toString()+":"+secondArg.text.toString()+"|"+expression
            }
            val call = newton.solve(operation,expression)

            call.enqueue( object : Callback<SolutionDTO> {
                override fun onFailure(call: Call<SolutionDTO>, t: Throwable) {
                    Log.d("am2019", "ups")
                }

                override fun onResponse(call: Call<SolutionDTO>, response: Response<SolutionDTO>) {
                    val body = response.body()
                    solution.text = body!!.result
                }
            })

        }

    }
}
