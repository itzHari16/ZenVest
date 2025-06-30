package com.example.zenvest.components

import android.graphics.Color
import android.graphics.Paint
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.zenvest.api.StockData
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry

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