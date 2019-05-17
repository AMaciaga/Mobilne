package com.example.calculations

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface NewtonAPI {
    @GET("/{operation}/{expression}")
    fun solve(@Path("operation")operation :String,@Path("expression") expression :String) : Call<SolutionDTO>
}