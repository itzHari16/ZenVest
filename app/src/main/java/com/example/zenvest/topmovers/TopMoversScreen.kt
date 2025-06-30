package com.example.zenvest.topmovers

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.zenvest.api.StockItem
import com.example.zenvest.components.AppColors
import com.example.zenvest.components.HorizontalStockSection
import com.example.zenvest.components.StockCard
import androidx.compose.ui.graphics.Color as ComposeColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopMoversScreen(
    viewModel: TopMoversViewModel = viewModel(),
    onViewAllClick: (String) -> Unit,
    onStockClick: (String, String, String?) -> Unit,
    onSearchClick: () -> Unit,
    onWatchlistClick: () -> Unit
) {
    val state = viewModel.topMoversState
    val isLoading = viewModel.isLoading
    val error = viewModel.errorMessage
    val isDummyData = viewModel.isDummyData

    LaunchedEffect(Unit) {
        viewModel.fetchTopMovers("53Q7L9RG7J5RCSJV") // Use invalid key for testing
    }

    Scaffold(
        containerColor = AppColors.Background,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    // .shadow(8.dp, RoundedCornerShape(bottom = 16.dp, topStart = 0.dp, topEnd = 0.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(AppColors.GradientStart, AppColors.GradientEnd)
                        )
                    )
            ) {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(start = 16.dp)
                        ) {
                            Text(
                                "ZenVest",
                                color = AppColors.TextPrimary,
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 28.sp
                                )
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            IconButton(onClick = onWatchlistClick)
                            {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "watchlist",
                                    tint = AppColors.AccentGreen
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent, // Use transparent to let Box background show
                        titleContentColor = AppColors.TextPrimary
                    ),
                    modifier = Modifier
                    // .clip(RoundedCornerShape(bottom = 16.dp, topStart = 0.dp, topEnd = 0.dp))
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.Background)
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 24.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(AppColors.CardBackground)
                    .clickable(onClick = onSearchClick)
                    .padding(16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search for Stock",
                        tint = AppColors.AccentGreen,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                    Text(
                        text = "Search for Stock",
                        color = AppColors.TextSecondary,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
            }

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = AppColors.AccentGreen,
                            strokeWidth = 4.dp
                        )
                    }
                }

                state != null -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.animateContentSize(tween(300))
                    ) {
                        item {
                            if (isDummyData && error != null) {
                                Text(
                                    text = "Showing sample data due to error: $error",
                                    color = Color.Yellow,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                        .background(
                                            AppColors.CardBackground,
                                            RoundedCornerShape(8.dp)
                                        )
                                        .padding(12.dp)
                                )
                            }
                        }
                        item {
                            HorizontalStockSection(
                                title = "Top Gainers",
                                stocks = state.topGainers ?: emptyList(),
                                onViewAllClick = { onViewAllClick("gainers") },
                                onStockClick = onStockClick
                            )
                        }
                        item {
                            HorizontalStockSection(
                                title = "Top Losers",
                                stocks = state.topLosers ?: emptyList(),
                                onViewAllClick = { onViewAllClick("losers") },
                                onStockClick = onStockClick
                            )
                        }
                        item {
                            HorizontalStockSection(
                                title = "Most Active",
                                stocks = state.mostActive ?: emptyList(),
                                onViewAllClick = { onViewAllClick("active") },
                                onStockClick = onStockClick
                            )
                        }
                    }
                }

                else -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No data available",
                            color = AppColors.TextSecondary,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullStockListScreen(
    title: String,
    stocks: List<StockItem>,
    onBack: () -> Unit,
    onStockClick: (String, String, String?) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        title,
                        color = AppColors.TextPrimary,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = AppColors.AccentGreen
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppColors.CardBackground
                )
            )
        },
        containerColor = AppColors.Background
    ) { paddingValues ->
        if (stocks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No $title available",
                    color = AppColors.TextSecondary,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 8.dp),
                contentPadding = PaddingValues(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(stocks) { stock ->
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
                        },
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .shadow(6.dp, RoundedCornerShape(12.dp))
                            .background(AppColors.CardBackground)
                    )
                }
            }
        }
    }
}

