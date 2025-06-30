package com.example.zenvest.api


import com.google.gson.annotations.SerializedName

data class StockResponse(
    @SerializedName("Meta Data") val metaData: MetaData,
    @SerializedName("Time Series (5min)") val timeSeries5min: Map<String, StockData>? = null,
    @SerializedName("Time Series (Daily)") val timeSeriesDaily: Map<String, StockData>? = null,
    @SerializedName("Weekly Time Series") val timeSeriesWeekly: Map<String, StockData>? = null,
    @SerializedName("Monthly Time Series") val timeSeriesMonthly: Map<String, StockData>? = null
)

data class StockData(
    @SerializedName("1. open") val open: String,
    @SerializedName("2. high") val high: String,
    @SerializedName("3. low") val low: String,
    @SerializedName("4. close") val close: String,
    @SerializedName("5. volume") val volume: String
)

data class MetaData(
    @SerializedName("1. Information") val information: String,
    @SerializedName("2. Symbol") val symbol: String,
    @SerializedName("3. Last Refreshed") val lastRefreshed: String,
    @SerializedName("4. Interval") val interval: String,
    @SerializedName("5. Output Size") val outputSize: String,
    @SerializedName("6. Time Zone") val timeZone: String
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
    //@SerializedName("volume") val volume: String
)
data class CompanyOverview(
    @SerializedName("Name")
    val name: String?,

    @SerializedName("Description")
    val description: String?,

    @SerializedName("Sector")
    val sector: String?,

    @SerializedName("Industry")
    val industry: String?,

    @SerializedName("MarketCapitalization")
    val marketCapitalization: String?,

    @SerializedName("PERatio")
    val peRatio: String?,

    @SerializedName("PEGRatio")
    val pegRatio: String?, // Added PEGRatio

    @SerializedName("DividendYield")
    val dividendYield: String?,

    @SerializedName("ProfitMargin")
    val profitMargin: String?,

    @SerializedName("Beta")
    val beta: String?,

    @SerializedName("52WeekHigh")
    val week52High: String?,

    @SerializedName("52WeekLow")
    val week52Low: String?,

    @SerializedName("price")
    val price: String,
)

data class SearchResponse(
    @SerializedName("bestMatches") val bestMatches: List<SymbolMatch>? = null
)

data class SymbolMatch(
    @SerializedName("1. symbol") val symbol: String,
    @SerializedName("2. name") val name: String,
    @SerializedName("3. type") val type: String,
    @SerializedName("4. region") val region: String,
    @SerializedName("5. marketOpen") val marketOpen: String,
    @SerializedName("6. marketClose") val marketClose: String,
    @SerializedName("7. timezone") val timezone: String,
    @SerializedName("8. currency") val currency: String,
    @SerializedName("9. matchScore") val matchScore: String
)

