package com.example.zenvest.api


import com.google.gson.annotations.SerializedName

data class StockResponse(
    @SerializedName("Time Series (1min)")
    val timeSeries: Map<String, StockData>
)

data class StockData(
    @SerializedName("1. open")
    val open: String
)

data class CompanyResponse(
    @SerializedName("Name")
    val name: String
)

data class TopMoversResponse(
    @SerializedName("top_gainers") val topGainers: List<StockItem>?,
    @SerializedName("top_losers") val topLosers: List<StockItem>?,
    @SerializedName("most_actively_traded") val mostActive: List<StockItem>?
)

data class StockItem(
    @SerializedName("ticker") val ticker: String,
    @SerializedName("price") val price: String,
    @SerializedName("change_amount") val changeAmount: String,
    @SerializedName("change_percentage") val changePercentage: String,
    @SerializedName("volume") val volume: String
)

