package com.example.zenvest.topmovers

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.zenvest.api.StockItem
import com.example.zenvest.components.StockCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopMoversScreen(
    viewModel: TopMoversViewModel = viewModel(),
    onViewAllClick: (String) -> Unit,
    onStockClick: (String) -> Unit
) {
    val state = viewModel.topMoversState
    val isLoading = viewModel.isLoading
    val error = viewModel.errorMessage

    LaunchedEffect(Unit) {
        viewModel.fetchTopMovers("53Q7L9RG7J5RCSJV")
    }

    Scaffold(
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0B0C1C))
                .padding(paddingValues)
        ) {
            when {
                isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                error != null -> Text(
                    "Error: $error",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )

                state != null -> {
                    LazyColumn {
                        item {
                            HorizontalStockSection(
                                title = "Top Gainers",
                                stocks = state.topGainers ?: emptyList(),
                                onViewAllClick = { onViewAllClick("gainers") }, onStockClick = onStockClick
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
    onStockClick: (String) -> Unit

) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        containerColor = Color(0xFF0B0C1C)
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(paddingValues)
                .background(Color(0xFF0B0C1C)),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(stocks) { stock ->
                StockCard(
                    name = stock.ticker,
                    price = stock.price,
                    change = "${stock.changeAmount} (${stock.changePercentage})",
                    //logoResId = getLogoResId(stock.ticker),
                    changeIsPositive = stock.changeAmount.toDoubleOrNull()?.let { it >= 0 } ?: true,
                    onClick = {onStockClick(stock.ticker)}
                )
            }
        }
    }
}

@Composable
fun HorizontalStockSection(
    title: String,
    stocks: List<StockItem>,
    onViewAllClick: () -> Unit,
    onStockClick: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )

            Text(
                text = "View All",
                color = Color(0xFF00FF66),
                modifier = Modifier
                    .padding(end = 4.dp)
                    .clickable { onViewAllClick() }
            )
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(stocks.take(4)) { stock ->
                StockCard(
                    name = stock.ticker,
                    price = stock.price,
                    change = "${stock.changeAmount} (${stock.changePercentage})",
                    //  logoResId = getLogoResId(stock.ticker),
                    changeIsPositive = stock.changeAmount.toDoubleOrNull()?.let { it >= 0 } ?: true,
                    onClick = { onStockClick(stock.ticker) }
                )
            }
        }
    }
}



