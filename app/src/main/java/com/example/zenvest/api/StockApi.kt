package com.example.zenvest.api

import okhttp3.Response
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

    @GET("query")
    suspend fun getIntradayData(
        @Query("function") function: String = "TIME_SERIES_INTRADAY",
        @Query("symbol") symbol: String,
        @Query("interval") interval: String = "5min",
        @Query("apikey") apikey: String,
        @Query("datatype") datatype: String = "json",
        @Query("outputsize") outputSize: String? = "compact", // Optional: compact or full
        @Query("adjusted") adjusted: Boolean? = true, // Optional: true or false
        @Query("extended_hours") extendedHours: Boolean? = true, // Optional: true or false
        @Query("month") month: String? = null // Optional: YYYY-MM format
    ): retrofit2.Response<StockResponse>

    @GET("query")
    suspend fun getCompanyoverview(
        @Query("symbol") symbol: String,
        @Query("function") function: String = "OVERVIEW",
        @Query("apikey") apiKey: String
    ): CompanyOverview

}