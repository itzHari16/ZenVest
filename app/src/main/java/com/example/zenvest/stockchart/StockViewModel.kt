package com.example.zenvest.stockchart


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zenvest.api.MetaData
import com.example.zenvest.api.RetrofitInstance
import com.example.zenvest.api.StockData
import com.example.zenvest.api.StockResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StockViewModel : ViewModel() {
    val apiKey = "53Q7L9RG7J5RCSJV"

    private val _dailyData = MutableStateFlow<List<StockData>>(emptyList())
    val dailyData: StateFlow<List<StockData>> = _dailyData

    private val _weeklyData = MutableStateFlow<List<StockData>>(emptyList())
    val weeklyData: StateFlow<List<StockData>> = _weeklyData

    private val _monthlyData = MutableStateFlow<List<StockData>>(emptyList())
    val monthlyData: StateFlow<List<StockData>> = _monthlyData

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _metaData = MutableStateFlow<MetaData?>(null)
    val metaData: StateFlow<MetaData?> = _metaData

    private val _latestPrice = MutableStateFlow<String?>(null)
    val latestPrice: StateFlow<String?> = _latestPrice

    enum class TimeInterval { DAILY, WEEKLY, MONTHLY }

    private suspend fun fetchData(symbol: String, interval: TimeInterval) {
        _isLoading.value = true
        _error.value = null
        try {
            val response = when (interval) {
                TimeInterval.DAILY -> RetrofitInstance.api.getDailyData(function = "TIME_SERIES_DAILY", symbol = symbol, apikey = apiKey)
                TimeInterval.WEEKLY -> RetrofitInstance.api.getWeeklyData(function = "TIME_SERIES_WEEKLY", symbol = symbol, apikey = apiKey)
                TimeInterval.MONTHLY -> RetrofitInstance.api.getMonthlyData(function = "TIME_SERIES_MONTHLY", symbol = symbol, apikey = apiKey)
            }
            if (response.isSuccessful) {
                response.body()?.let { stockResponse ->
                    val data = stockResponse.getTimeSeries(interval)?.entries
                        ?.sortedBy { it.key }
                        ?.map { it.value }
                        ?.also { println("${interval.name} Data: $it") } ?: emptyList()
                    when (interval) {
                        TimeInterval.DAILY -> {
                            _dailyData.value = data
                            _latestPrice.value = data.firstOrNull()?.close // Set latest price from daily data
                        }
                        TimeInterval.WEEKLY -> _weeklyData.value = data
                        TimeInterval.MONTHLY -> _monthlyData.value = data
                    }
                    _metaData.value = stockResponse.metaData
                } ?: run {
                    _error.value = "No ${interval.name.lowercase()} data received"
                }
            } else {
                _error.value = "${interval.name} API error ${response.code()}: ${response.message()}"
            }
        } catch (e: Exception) {
            _error.value = "${interval.name} network error: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    fun fetchDailyData(symbol: String) = viewModelScope.launch { fetchData(symbol, TimeInterval.DAILY) }
    fun fetchWeeklyData(symbol: String) = viewModelScope.launch { fetchData(symbol, TimeInterval.WEEKLY) }
    fun fetchMonthlyData(symbol: String) = viewModelScope.launch { fetchData(symbol, TimeInterval.MONTHLY) }

    fun fetchAllData(symbol: String) = viewModelScope.launch {
        listOf(TimeInterval.DAILY, TimeInterval.WEEKLY, TimeInterval.MONTHLY).forEach {
            fetchData(symbol, it)
        }
    }

    private fun StockResponse.getTimeSeries(interval: TimeInterval): Map<String, StockData>? = when (interval) {
        TimeInterval.DAILY -> timeSeriesDaily
        TimeInterval.WEEKLY -> timeSeriesWeekly
        TimeInterval.MONTHLY -> timeSeriesMonthly
    }
}