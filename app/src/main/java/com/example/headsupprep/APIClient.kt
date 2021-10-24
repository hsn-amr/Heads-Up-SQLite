package com.example.headsupprep

import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit

class APIClient {

    private var retrofit: Retrofit? = null

    fun getClient(): Retrofit? {
        retrofit = Retrofit.Builder()
            .baseUrl("https://dojo-recipes.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit
    }
}