package com.example.zenvest.api

import retrofit2.http.GET
import retrofit2.http.Query

interface StockApi {
    @GET("query?function=TIME_SERIES_INTRADAY&interval=1min")
    suspend fun getStockPrice(
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String
    ): StockResponse

    @GET("query?function=OVERVIEW")
    suspend fun getCompanyDetails(
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String
    ): CompanyResponse

    @GET("query")
    suspend fun getTopMovers(
        @Query("function") function: String = "TOP_GAINERS_LOSERS",
        @Query("apikey") apiKey: String
    ): retrofit2.Response<TopMoversResponse>
}