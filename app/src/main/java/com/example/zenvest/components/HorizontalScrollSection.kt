package com.example.zenvest.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.zenvest.api.StockItem

@Composable
fun HorizontalStockSection(
    title: String,
    stocks: List<StockItem>,
    onViewAllClick: () -> Unit,
    onStockClick: (String, String, String?) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(tween(300))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = AppColors.TextPrimary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "View All",
                color = AppColors.AccentGreen,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(AppColors.AccentPurple, AppColors.AccentGreen)
                        )
                    )
                    .clickable { onViewAllClick() }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
        if (stocks.isEmpty()) {
            Text(
                text = "No $title available",
                color = AppColors.TextSecondary,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(AppColors.CardBackground)
                    .padding(12.dp)
            )
        } else {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(stocks.take(4)) { stock ->
                    StockCard(
                        name = stock.ticker,
                        price = stock.price,
                        change = "${stock.changeAmount} (${stock.changePercentage})",
                        changeIsPositive = stock.changeAmount.toDoubleOrNull()?.let { it >= 0 }
                            ?: true,
                        onClick = {
                            onStockClick(
                                stock.ticker,
                                stock.price,
                                stock.changePercentage
                            )
                        }
                    )
                }
            }
        }
    }
}
