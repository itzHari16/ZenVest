//package com.example.stockapp.ui
//
//import android.graphics.Color
//import android.graphics.Paint
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.viewinterop.AndroidView
//import com.example.zenvest.api.MetaData
//import com.example.zenvest.api.StockData
//import com.example.zenvest.stockchart.StockViewModel
//import com.github.mikephil.charting.charts.CandleStickChart
//import com.github.mikephil.charting.data.CandleData
//import com.github.mikephil.charting.data.CandleDataSet
//import com.github.mikephil.charting.data.CandleEntry
//import com.google.accompanist.pager.ExperimentalPagerApi
//import com.google.accompanist.pager.HorizontalPager
//import com.google.accompanist.pager.rememberPagerState
//import androidx.compose.ui.graphics.Color as ComposeColor
//
//@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
//@Composable
//fun StockScreenContent(
//    symbol: String,
//    viewModel: StockViewModel,
//    modifier: Modifier = Modifier
//) {
//    var selectedTabIndex by remember { mutableStateOf(0) }
//    val pagerState = rememberPagerState(initialPage = selectedTabIndex)
//    val dailyData by viewModel.dailyData.collectAsState()
//    val weeklyData by viewModel.weeklyData.collectAsState()
//    val monthlyData by viewModel.monthlyData.collectAsState()
//    val isLoading by viewModel.isLoading.collectAsState()
//    val error by viewModel.error.collectAsState()
//    val metaData by viewModel.metaData.collectAsState()
//
//    LaunchedEffect(selectedTabIndex) {
//        pagerState.animateScrollToPage(selectedTabIndex)
//        when (selectedTabIndex) {
//            0 -> viewModel.fetchDailyData(symbol)
//            1 -> viewModel.fetchWeeklyData(symbol)
//            2 -> viewModel.fetchMonthlyData(symbol)
//        }
//    }
//
//    LaunchedEffect(pagerState.currentPage) {
//        selectedTabIndex = pagerState.currentPage
//    }
//
//    Column(
//        modifier = modifier,
//        verticalArrangement = Arrangement.spacedBy(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        TabRow(
//            selectedTabIndex = selectedTabIndex,
//            containerColor = ComposeColor(0xFF1C2526),
//            contentColor = ComposeColor(0xFF00FF66),
//            indicator = { tabPositions ->
//                TabRowDefaults.Indicator(
//                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
//                    color = ComposeColor(0xFF00FF66)
//                )
//            }
//        ) {
//            Tab(
//                selected = selectedTabIndex == 0,
//                onClick = { selectedTabIndex = 0 },
//                text = { Text("Daily", color = if (selectedTabIndex == 0) ComposeColor(0xFF00FF66) else ComposeColor.White) }
//            )
//            Tab(
//                selected = selectedTabIndex == 1,
//                onClick = { selectedTabIndex = 1 },
//                text = { Text("Weekly", color = if (selectedTabIndex == 1) ComposeColor(0xFF00FF66) else ComposeColor.White) }
//            )
//            Tab(
//                selected = selectedTabIndex == 2,
//                onClick = { selectedTabIndex = 2 },
//                text = { Text("Monthly", color = if (selectedTabIndex == 2) ComposeColor(0xFF00FF66) else ComposeColor.White) }
//            )
//        }
//
//        HorizontalPager(
//            count = 3,
//            state = pagerState,
//            modifier = Modifier.fillMaxWidth()
//        ) { page ->
//            ChartContent(
//                stockData = when (page) {
//                    0 -> dailyData
//                    1 -> weeklyData
//                    2 -> monthlyData
//                    else -> emptyList()
//                },
//                title = when (page) {
//                    0 -> "Daily Chart"
//                    1 -> "Weekly Chart"
//                    2 -> "Monthly Chart"
//                    else -> "Chart"
//                },
//                isLoading = isLoading,
//                error = error
//            )
//        }
//
//    }
//}
//
//@Composable
//fun ChartContent(
//    stockData: List<StockData>,
//    title: String,
//    isLoading: Boolean,
//    error: String?
//) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp),
//        elevation = CardDefaults.cardElevation(8.dp),
//        colors = CardDefaults.cardColors(containerColor = ComposeColor(0xFF1C2526))
//    ) {
//        Column(
//            modifier = Modifier.padding(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(
//                text = title,
//                style = MaterialTheme.typography.headlineMedium.copy(color = ComposeColor.White),
//                modifier = Modifier.padding(bottom = 12.dp)
//            )
//            when {
//                isLoading -> CircularProgressIndicator(color = ComposeColor(0xFF00FF66))
//                error != null -> Text(
//                    text = error,
//                    color = ComposeColor(0xFFFF3D3D),
//                    textAlign = TextAlign.Center,
//                    style = MaterialTheme.typography.bodyMedium,
//                    modifier = Modifier.fillMaxWidth().padding(8.dp)
//                )
//                stockData.isNotEmpty() -> CandlestickChart(
//                    stockData = stockData,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(300.dp)
//                )
//                else -> Text(
//                    text = "No data available",
//                    color = ComposeColor.White,
//                    textAlign = TextAlign.Center,
//                    style = MaterialTheme.typography.bodyMedium,
//                    modifier = Modifier.fillMaxWidth().padding(8.dp)
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun CandlestickChart(
//    stockData: List<StockData>,
//    modifier: Modifier = Modifier
//) {
//    AndroidView(
//        modifier = modifier,
//        factory = { context ->
//            CandleStickChart(context).apply {
//                setBackgroundColor(Color.parseColor("#0B0C1C"))
//                description.isEnabled = false
//                setDrawGridBackground(false)
//
//                xAxis.apply {
//                    setDrawGridLines(false)
//                    setDrawLabels(false)
//                    position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
//                    textColor = Color.WHITE
//                }
//
//                axisLeft.apply {
//                    setDrawGridLines(true)
//                    setLabelCount(5, true)
//                    setDrawLabels(true)
//                    setDrawAxisLine(true)
//                    textColor = Color.WHITE
//                    gridColor = Color.parseColor("#1C2526")
//                    textSize = 10f
//                }
//
//                axisRight.isEnabled = false
//
//                legend.isEnabled = true
//                setScaleEnabled(true)
//                setPinchZoom(true)
//            }
//        },
//        update = { chart ->
//            val entries = stockData.mapIndexed { index, data ->
//                CandleEntry(
//                    index.toFloat(),
//                    data.high.toFloatOrNull() ?: 0f,
//                    data.low.toFloatOrNull() ?: 0f,
//                    data.open.toFloatOrNull() ?: 0f,
//                    data.close.toFloatOrNull() ?: 0f
//                )
//            }
//
//            chart.axisLeft.apply {
//                val prices = entries.flatMap { listOf(it.high, it.low, it.open, it.close) }
//                    .filterNot { it.isNaN() }
//                if (prices.isNotEmpty()) {
//                    axisMinimum = prices.min() * 0.95f
//                    axisMaximum = prices.max() * 1.05f
//                } else {
//                    axisMinimum = 0f
//                    axisMaximum = 100f
//                }
//            }
//
//            val dataSet = CandleDataSet(entries, "Stock Data").apply {
//                setDrawIcons(false)
//                shadowColor = Color.parseColor("#404040")
//                shadowWidth = 0.7f
//                decreasingColor = Color.parseColor("#FF3D3D")
//                decreasingPaintStyle = Paint.Style.FILL
//                increasingColor = Color.parseColor("#00FF66")
//                increasingPaintStyle = Paint.Style.FILL
//                neutralColor = Color.parseColor("#0000FF")
//                setDrawValues(false)
//            }
//
//            chart.data = CandleData(dataSet)
//            chart.notifyDataSetChanged()
//            chart.invalidate()
//        }
//    )
//}



package com.example.zenvest.ui

import android.graphics.Color
import android.graphics.Paint
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.zenvest.api.StockData
import com.example.zenvest.stockchart.StockViewModel
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import androidx.compose.ui.graphics.Color as ComposeColor

// Color Palette
object AppColors {
    val Background = ComposeColor(0xFF0F0F1F)
    val CardBackground = ComposeColor(0xFF1C1C2E)
    val AccentGreen = ComposeColor(0xFF00FF66)
    val AccentRed = ComposeColor(0xFFFF3D3D)
    val AccentPurple = ComposeColor(0xFF7C3AED)
    val TextPrimary = ComposeColor.White
    val TextSecondary = ComposeColor(0xFFBBBBBB)
    val TextTertiary = ComposeColor(0xFF6B7280)
    val ChartBackground = ComposeColor(0xFF0B0C1C)
    val Divider = ComposeColor(0xFF3B3B5B)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun StockScreenContent(
    symbol: String,
    viewModel: StockViewModel,
    modifier: Modifier = Modifier
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val pagerState = rememberPagerState(initialPage = selectedTabIndex)
    val dailyData by viewModel.dailyData.collectAsState()
    val weeklyData by viewModel.weeklyData.collectAsState()
    val monthlyData by viewModel.monthlyData.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
        when (selectedTabIndex) {
            0 -> viewModel.fetchDailyData(symbol)
            1 -> viewModel.fetchWeeklyData(symbol)
            2 -> viewModel.fetchMonthlyData(symbol)
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        selectedTabIndex = pagerState.currentPage
    }

    Column(
        modifier = modifier.background(AppColors.Background),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = AppColors.CardBackground,
            contentColor = AppColors.AccentGreen,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[selectedTabIndex])
                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)),
                    color = AppColors.AccentGreen,
                    height = 4.dp
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(8.dp))
        ) {
            listOf("Daily", "Weekly", "Monthly").forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    modifier = Modifier.padding(vertical = 12.dp),
                    text = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            color = if (selectedTabIndex == index) AppColors.AccentGreen else AppColors.TextSecondary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                )
            }
        }

        HorizontalPager(
            count = 3,
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            ChartContent(
                stockData = when (page) {
                    0 -> dailyData
                    1 -> weeklyData
                    2 -> monthlyData
                    else -> emptyList()
                },
                isLoading = isLoading,
                error = error,
            )
        }
    }
}

@Composable
fun ChartContent(
    stockData: List<StockData>,
    isLoading: Boolean,
    error: String?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(12.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.CardBackground)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = AppColors.AccentPurple,
                            strokeWidth = 4.dp
                        )
                    }
                }
                error != null -> Text(
                    text = error,
                    color = AppColors.AccentRed,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(AppColors.Background, RoundedCornerShape(8.dp))
                )
                stockData.isNotEmpty() -> CandlestickChart(
                    stockData = stockData,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .border(1.dp, AppColors.Divider, RoundedCornerShape(8.dp))
                )
                else -> Text(
                    text = "No data available",
                    color = AppColors.TextSecondary,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
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
                setBackgroundColor(Color.parseColor("#0B0C1C"))
                description.isEnabled = false
                setDrawGridBackground(false)

                xAxis.apply {
                    setDrawGridLines(false)
                    setDrawLabels(false)
                    position = XAxis.XAxisPosition.BOTTOM
                    textColor = Color.WHITE
                }

                axisLeft.apply {
                    setDrawGridLines(true)
                    setLabelCount(5, true)
                    setDrawLabels(true)
                    setDrawAxisLine(true)
                    textColor = Color.WHITE
                    gridColor = Color.parseColor("#1C2526")
                    textSize = 12f
                }

                axisRight.isEnabled = false
                legend.isEnabled = true
                setScaleEnabled(true)
                setPinchZoom(true)
            }
        },
        update = { chart ->
            val entries = stockData.mapIndexed { index, data ->
                CandleEntry(
                    index.toFloat(),
                    data.high.toFloatOrNull() ?: 0f,
                    data.low.toFloatOrNull() ?: 0f,
                    data.open.toFloatOrNull() ?: 0f,
                    data.close.toFloatOrNull() ?: 0f
                )
            }

            chart.axisLeft.apply {
                val prices = entries.flatMap { listOf(it.high, it.low, it.open, it.close) }
                    .filterNot { it.isNaN() }
                if (prices.isNotEmpty()) {
                    axisMinimum = prices.min() * 0.95f
                    axisMaximum = prices.max() * 1.05f
                } else {
                    axisMinimum = 0f
                    axisMaximum = 100f
                }
            }

            val dataSet = CandleDataSet(entries, "Stock Data").apply {
                setDrawIcons(false)
                shadowColor = Color.parseColor("#404040")
                shadowWidth = 0.7f
                decreasingColor = Color.parseColor("#FF3D3D")
                decreasingPaintStyle = Paint.Style.FILL
                increasingColor = Color.parseColor("#00FF66")
                increasingPaintStyle = Paint.Style.FILL
                neutralColor = Color.parseColor("#7C3AED")
                setDrawValues(false)
            }

            chart.data = CandleData(dataSet)
            chart.notifyDataSetChanged()
            chart.invalidate()
        }
    )
}