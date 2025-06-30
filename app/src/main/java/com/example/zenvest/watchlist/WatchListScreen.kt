package com.example.zenvest.watchlist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.zenvest.components.StockCard
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun WatchlistScreen(
//    onStockClick: (String, String, String?) -> Unit,
//    onBack: () -> Unit,
//    viewModel: WatchlistViewModel = viewModel()
//) {
//    val watchlist by viewModel.watchlist.collectAsState(initial = emptyList())
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Your Watchlist") },
//                navigationIcon = {
//                    IconButton(onClick = onBack) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
//                    }
//                }
//            )
//        }
//    ) { padding ->
//        if (watchlist.isEmpty()) {
//            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                Text("No stocks in watchlist")
//            }
//        } else {
//            LazyColumn(modifier = Modifier.padding(padding)) {
//                items(watchlist) { item ->
//                    StockCard(
//                        name = item.symbol,
//                        price = item.price,
//                        change = item.changePercentage ?: "",
//                        changeIsPositive = item.changePercentage?.contains("-") == false,
//                        onClick = { onStockClick(item.symbol, item.price, item.changePercentage) },
//                        modifier = Modifier.padding(8.dp)
//                    )
//                }
//            }
//        }
//    }
//}

@Composable
fun WatchlistScreen(
    navController: NavHostController,
    viewModel: WatchlistViewModel = viewModel(
        factory = WatchlistViewModelFactory(
            WatchlistRepository(AppDatabase.getDatabase(LocalContext.current).watchlistDao())
        )
    )
) {
    val watchlist by viewModel.watchlist.collectAsState(initial = emptyList())
    when {
        watchlist == null -> CircularProgressIndicator() // Loading state
        watchlist.isEmpty() -> Text(
            text = "Watchlist is empty",
            modifier = Modifier.fillMaxSize(),
            textAlign = TextAlign.Center
        )
        else -> LazyColumn {
            items(watchlist) { item ->
                StockCard(
                    name = item.symbol,
                    price = item.price,
                    change = item.changePercentage ?: "N/A",
                    changeIsPositive = item.changePercentage?.contains("-") == false,
                    onClick = {
                        navController.navigate(
                            "company_overview/${item.symbol}" +
                                    "?price=${item.price}" +
                                    "&changePercentage=${item.changePercentage ?: "N/A"}"
                        )
                    }
                )
            }
        }
    }
}