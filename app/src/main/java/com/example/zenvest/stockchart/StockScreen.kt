package com.example.stockapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zenvest.api.RetrofitInstance
import com.example.zenvest.api.StockData
import com.example.zenvest.api.StockRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.graphics.Color
import android.graphics.Paint
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel

class StockViewModel : ViewModel() {
    private val _stockData = MutableStateFlow<List<StockData>>(emptyList())
    val stockData: StateFlow<List<StockData>> = _stockData

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    val apikey = "53Q7L9RG7J5RCSJV"

    fun fetchStockData(symbol: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = RetrofitInstance.api.getIntradayData(
                    symbol = symbol,
                    apikey = apikey,
                    interval = "5min",
                    outputSize = "compact"
                )
                if (response.isSuccessful) {
                    response.body()?.let { stockResponse ->
                        _stockData.value = stockResponse.timeSeries.entries
                            .sortedBy { it.key } // Sort by timestamp
                            .map { it.value }
                    } ?: run {
                        _error.value = "No data received from the server"
                    }
                } else {
                    _error.value = "API error: ${response.code()} - ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Network error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}



@Composable
fun CandlestickChart(
    stockData: List<StockData>,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            CandleStickChart(context).apply {
                // General chart settings
                setBackgroundColor(Color.WHITE)
                description.isEnabled = false
                setDrawGridBackground(false)

                // X-axis configuration
                xAxis.apply {
                    setDrawGridLines(false)
                    setDrawLabels(false) // Hide timestamps for simplicity
                    position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
                }

                // Left Y-axis configuration
                axisLeft.apply {
                    setDrawGridLines(true) // Enable horizontal grid lines
                    setLabelCount(5, true) // Force 5 labels
                    setDrawLabels(true) // Ensure labels are visible
                    setDrawAxisLine(true) // Draw the axis line
                    textColor = Color.BLACK // Set label text color
                    gridColor = Color.LTGRAY // Set grid line color
                    textSize = 10f // Set label text size
                }

                // Right Y-axis configuration (disable to avoid duplication)
                axisRight.isEnabled = false

                // Legend and interaction settings
                legend.isEnabled = true
                setScaleEnabled(true)
                setPinchZoom(true)
            }
        },
        update = { chart ->
            // Convert stock data to CandleEntry
            val entries = stockData.mapIndexed { index, data ->
                CandleEntry(
                    index.toFloat(),
                    data.high.toFloatOrNull() ?: 0f,
                    data.low.toFloatOrNull() ?: 0f,
                    data.open.toFloatOrNull() ?: 0f,
                    data.close.toFloatOrNull() ?: 0f
                )
            }
            // Update Y-axis range based on data
            chart.axisLeft.apply {
                val prices = entries.flatMap { listOf(it.high, it.low, it.open, it.close) }
                    .filterNot { it.isNaN() }
                if (prices.isNotEmpty()) {
                    axisMinimum = prices.min() * 0.95f // 5% below min for padding
                    axisMaximum = prices.max() * 1.05f // 5% above max for padding
                } else {
                    // Fallback range if no valid data
                    axisMinimum = 0f
                    axisMaximum = 100f
                }
            }
            // Create dataset
            val dataSet = CandleDataSet(entries, "Stock Data").apply {
                setDrawIcons(false)
                shadowColor = Color.DKGRAY
                shadowWidth = 0.7f
                decreasingColor = Color.RED
                decreasingPaintStyle = Paint.Style.FILL
                increasingColor = Color.GREEN
                increasingPaintStyle = Paint.Style.FILL
                neutralColor = Color.BLUE
                setDrawValues(false)
            }

            // Set data to chart
            chart.data = CandleData(dataSet)
            chart.notifyDataSetChanged() // Notify chart of data changes
            chart.invalidate() // Refresh the chart
        }
    )
}




@Composable
fun StockGraph(
    symbol: String = "IBM",
    viewModel: StockViewModel = viewModel()
) {
    val stockData by viewModel.stockData.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(symbol) {
        viewModel.fetchStockData(symbol = symbol)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> CircularProgressIndicator()
            error != null -> Text(
                text = error ?: "Unknown error",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxSize()
            )
            stockData.isNotEmpty() -> CandlestickChart(
                stockData = stockData,
                modifier = Modifier.fillMaxSize()
            )
            else -> Text(
                text = "No data available",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}