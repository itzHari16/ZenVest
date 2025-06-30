package com.example.zenvest.api

import com.github.mikephil.charting.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StockRepository {
    private val api = RetrofitInstance.api
    private val apiKey = "53Q7L9RG7J5RCSJV"

    suspend fun fetchStockPrice(symbol: String): Double {
        val response = api.getStockPrice(symbol, apiKey)
        val latestTime = response.timeSeries5min?.keys?.first()
        return response.timeSeries5min?.get(latestTime)?.open?.toDouble() ?: 0.0
    }

    suspend fun fetchCompanyName(symbol: String): String {
        return api.getCompanyDetails(symbol, apiKey).name
    }

    suspend fun getCompanyOverview(symbol: String): CompanyOverview {
        return api.getCompanyoverview(symbol = symbol, apiKey = apiKey)
    }


}