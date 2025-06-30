package com.example.zenvest.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://www.alphavantage.co/"
    val api: StockApi by lazy {
        val gson = GsonBuilder()
            .setLenient() // Handle malformed JSON gracefully
            .create()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(StockApi::class.java)
    }
}