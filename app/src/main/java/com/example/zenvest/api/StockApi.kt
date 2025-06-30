package com.example.zenvest.api

import retrofit2.Response
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
    suspend fun getDailyData(
        @Query("function") function: String = "TIME_SERIES_DAILY",
        @Query("symbol") symbol: String,
        @Query("outputsize") outputSize: String? = "compact",
        @Query("apikey") apikey: String,
        @Query("datatype") datatype: String = "json"
    ): Response<StockResponse>


    @GET("query")
    suspend fun getWeeklyData(
        @Query("function") function: String = "TIME_SERIES_WEEKLY",
        @Query("symbol") symbol: String,
        @Query("apikey") apikey: String,
        @Query("datatype") datatype: String = "json"
    ): Response<StockResponse>

    @GET("query")
    suspend fun getMonthlyData(
        @Query("function") function: String = "TIME_SERIES_MONTHLY",
        @Query("symbol") symbol: String,
        @Query("apikey") apikey: String,
        @Query("datatype") datatype: String = "json"
    ): Response<StockResponse>

    @GET("query")
    suspend fun getCompanyoverview(
        @Query("symbol") symbol: String,
        @Query("function") function: String = "OVERVIEW",
        @Query("apikey") apiKey: String
    ): CompanyOverview

    @GET("query")
    suspend fun searchSymbols(
        @Query("function") function: String = "SYMBOL_SEARCH",
        @Query("keywords") keywords: String,
        @Query("apikey") apikey: String,
        @Query("datatype") datatype: String = "json"
    ): Response<SearchResponse>
}