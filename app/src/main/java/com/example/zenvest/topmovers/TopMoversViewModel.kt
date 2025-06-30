package com.example.zenvest.topmovers

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zenvest.api.RetrofitInstance
import com.example.zenvest.api.StockItem
import com.example.zenvest.api.TopMoversResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

open class TopMoversViewModel @Inject constructor() : ViewModel() {
    open var topMoversState: TopMoversResponse? by mutableStateOf<TopMoversResponse?>(null)
    open var errorMessage by mutableStateOf<String?>(null)
    open var isLoading by mutableStateOf(false)
    open var isDummyData by mutableStateOf(false)
    private var fetchJob: Job? = null

    fun fetchTopMovers(apiKey: String) {
        fetchJob?.cancel() // Cancel any ongoing fetch
        fetchJob = viewModelScope.launch {
            isLoading = true
            isDummyData = false
            Log.d("TopMoversViewModel", "Fetching top movers with API key: $apiKey")
            try {
                val response = RetrofitInstance.api.getTopMovers(apiKey = apiKey)
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null && (data.topGainers?.isNotEmpty() == true ||
                                data.topLosers?.isNotEmpty() == true ||
                                data.mostActive?.isNotEmpty() == true)
                    ) {
                        topMoversState = data
                        errorMessage = null
                        Log.d(
                            "TopMoversViewModel",
                            "API success: ${data.topGainers?.size} gainers, ${data.topLosers?.size} losers, ${data.mostActive?.size} active"
                        )
                    } else {
                        errorMessage = "API returned empty or invalid data"
                        topMoversState = getDummyTopMovers()
                        isDummyData = true
                        Log.w("TopMoversViewModel", "API returned empty data, using dummy data")
                    }
                } else {
                    errorMessage = "API failed: ${response.message()} (Code: ${response.code()})"
                    topMoversState = getDummyTopMovers()
                    isDummyData = true
                    Log.e(
                        "TopMoversViewModel",
                        "API error: ${response.message()} (Code: ${response.code()})"
                    )
                }
            } catch (e: Exception) {
                if (isActive) { // Only update state if coroutine is still active
                    errorMessage = "Network error: ${e.message}"
                    topMoversState = getDummyTopMovers()
                    isDummyData = true
                    Log.e("TopMoversViewModel", "Exception: ${e.message}", e)
                }
            }
            isLoading = false
            Log.d(
                "TopMoversViewModel",
                "Loading finished, isDummyData: $isDummyData, errorMessage: $errorMessage"
            )
        }
    }

    override fun onCleared() {
        fetchJob?.cancel() // Cancel coroutine when ViewModel is cleared
        super.onCleared()
    }

    private fun getDummyTopMovers(): TopMoversResponse {
        val dummyGainers = listOf(
            StockItem("AAPL", "150.25", "+2.50", "+1.69%"),
            StockItem("MSFT", "305.10", "+3.20", "+1.06%"),
            StockItem("GOOGL", "2750.80", "+25.30", "+0.93%"),
            StockItem("AMZN", "3400.45", "+15.75", "+0.46%")
        )
        val dummyLosers = listOf(
            StockItem("TSLA", "750.20", "-10.50", "-1.38%"),
            StockItem("NFLX", "580.15", "-8.25", "-1.40%"),
            StockItem("FB", "330.60", "-5.10", "-1.52%"),
            StockItem("NVDA", "220.30", "-3.80", "-1.69%")
        )
        val dummyMostActive = listOf(
            StockItem("AMC", "40.15", "+1.20", "+3.08%"),
            StockItem("GME", "180.50", "+2.75", "+1.55%"),
            StockItem("BB", "10.25", "-0.15", "-1.44%"),
            StockItem("NIO", "35.80", "+0.65", "+1.85%")
        )
        return TopMoversResponse(
            topGainers = dummyGainers,
            topLosers = dummyLosers,
            mostActive = dummyMostActive
        )
    }
}