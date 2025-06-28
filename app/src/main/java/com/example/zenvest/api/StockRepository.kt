package com.example.zenvest.api

class StockRepository {
    private val api = RetrofitInstance.api
    private val apiKey = "53Q7L9RG7J5RCSJV"

    suspend fun fetchStockPrice(symbol: String): Double {
        val response = api.getStockPrice(symbol, apiKey)
        val latestTime = response.timeSeries.keys.first()
        return response.timeSeries[latestTime]?.open?.toDouble() ?: 0.0
    }

    suspend fun fetchCompanyName(symbol: String): String {
        return api.getCompanyDetails(symbol, apiKey).name
    }
}