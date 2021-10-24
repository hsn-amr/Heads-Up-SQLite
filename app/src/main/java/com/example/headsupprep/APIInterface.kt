package com.example.headsupprep

import retrofit2.Call
import retrofit2.http.*

interface APIInterface {

    @Headers("Content-Type: application/json")
    @GET("/celebrities/")
    fun getCelebrities(): Call<List<Celebrity>>

    @Headers("content-type: application/json")
    @POST("/celebrities/")
    fun addNewCelebrity(@Body celebrity: Celebrity): Call<Celebrity>

    @Headers("content-type: application/json")
    @PUT("/celebrities/{id}")
    fun updateCelebrity(@Path("id") id: Int, @Body celebrity: Celebrity): Call<Celebrity>

    @Headers("content-type: application/json")
    @DELETE("/celebrities/{id}")
    fun deleteCelebrity(@Path("id") id: Int): Call<Void>

}