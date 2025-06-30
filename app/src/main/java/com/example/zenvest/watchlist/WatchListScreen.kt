import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
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
import com.example.zenvest.watchlist.AppDatabase
import com.example.zenvest.watchlist.WatchlistRepository
import com.example.zenvest.watchlist.WatchlistViewModel
import com.example.zenvest.watchlist.WatchlistViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistScreen(
    navController: NavHostController,
    onBack: () -> Unit = {},
    viewModel: WatchlistViewModel = viewModel(
        factory = WatchlistViewModelFactory (
            WatchlistRepository(AppDatabase.getDatabase(LocalContext.current).watchlistDao())
        )
    )
) {
    val watchlist by viewModel.watchlist.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Watchlist", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                watchlist == null -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                watchlist.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No stocks in your watchlist",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Add stocks from the Company screen to see them here.",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
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
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp) // adjust as needed
                            )
                        }
                    }
                }
            }
        }
    }
}
