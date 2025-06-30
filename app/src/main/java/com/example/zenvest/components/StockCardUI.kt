package com.example.zenvest.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Color Palette
object AppColors {
    val Background = Color(0xFF0B0C1C)
    val CardBackground = Color(0xFF1C2526)
    val AccentGreen = Color(0xFF00FF66)
    val AccentRed = Color(0xFFFF3D3D)
    val AccentPurple = Color(0xFF7C3AED)
    val TextPrimary = Color.White
    val TextSecondary = Color(0xFFBBBBBB)
    val TextTertiary = Color(0xFF6B7280)
    val GradientStart = Color(0xFF4A2E8A)
    val GradientEnd = Color(0xFF1C2526)
    val Divider = Color(0xFF3B3B5B)
}

@Composable
fun StockCard(
    name: String,
    price: String,
    change: String,
    changeIsPositive: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val changeColor = if (changeIsPositive) AppColors.AccentGreen else AppColors.AccentRed
    val initial = name.firstOrNull()?.toString() ?: "S"

    Box(
        modifier = modifier
            .padding(8.dp)
            .width(200.dp)
            .height(160.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        AppColors.GradientStart.copy(alpha = 0.9f),
                        AppColors.GradientEnd.copy(alpha = 0.8f)
                    )
                )
            )
            //.border(1.dp, AppColors.Divider.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
            //.shadow(10.dp, RoundedCornerShape(10.dp), clip = false)
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Row with Initial and Name
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                listOf(AppColors.AccentPurple, AppColors.AccentGreen)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initial,
                        style = TextStyle(
                            color = AppColors.TextPrimary,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 22.sp
                        )
                    )
                }

                Text(
                    text = name,
                    style = TextStyle(
                        color = AppColors.TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            // Price Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "$${price.toDoubleOrNull()?.let { String.format("%.2f", it) } ?: price}",
                    style = TextStyle(
                        color = AppColors.TextPrimary,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black
                    )
                )
            }

            // Change Badge
            Box(
                modifier = Modifier
                    .align(Alignment.End)
                    .clip(RoundedCornerShape(50))
                    .background(changeColor.copy(alpha = 0.15f))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = change,
                    style = TextStyle(
                        color = changeColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StockCardPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Background)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        StockCard(
            name = "TSLA",
            price = "251.76",
            change = "-5.12 (-2.03%)",
            changeIsPositive = false,
            onClick = {}
        )
    }
}

