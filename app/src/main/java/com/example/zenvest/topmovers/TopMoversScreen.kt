package com.example.zenvest.topmovers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopMoversScreen(viewModel: TopMoversViewModel = viewModel()) {
    val state = viewModel.topMoversState
    val isLoading = viewModel.isLoading
    val error = viewModel.errorMessage

    LaunchedEffect(Unit) {
        viewModel.fetchTopMovers("53Q7L9RG7J5RCSJV")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Top Movers") }
            )
        }
    ) { paddingValues -> // 👈 FIX: Scaffold content padding
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {

            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                error != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Error: $error", color = Color.Red)
                    }
                }

                state != null -> {
                    LazyColumn(modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)) {

                        item {
                            Text("Top Gainers", style = MaterialTheme.typography.titleLarge)
                        }
                        items(state.topGainers ?: emptyList()) { stock ->
                            StockItemCard(stock)
                        }

                        item { Spacer(modifier = Modifier.height(16.dp)) }
                        item {
                            Text("Top Losers", style = MaterialTheme.typography.titleLarge)
                        }
                        items(state.topLosers ?: emptyList()) { stock ->
                            StockItemCard(stock)
                        }

                        item { Spacer(modifier = Modifier.height(16.dp)) }
                        item {
                            Text("Most Active", style = MaterialTheme.typography.titleLarge)
                        }
                        items(state.mostActive ?: emptyList()) { stock ->
                            StockItemCard(stock)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StockItemCard(item: StockItem) {
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("${item.ticker} - \$${item.price}", fontWeight = FontWeight.Bold)
            Text("Change: ${item.changeAmount} (${item.changePercentage})")
            Text("Volume: ${item.volume}")
        }
    }
}
