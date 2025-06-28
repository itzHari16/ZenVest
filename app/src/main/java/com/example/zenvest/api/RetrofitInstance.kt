package com.example.zenvest.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: StockApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.alphavantage.co/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(StockApi::class.java)
    }
}