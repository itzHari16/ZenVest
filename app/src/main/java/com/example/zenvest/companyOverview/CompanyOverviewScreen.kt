//package com.example.zenvest.companyOverview
//
//import androidx.compose.animation.animateContentSize
//import androidx.compose.animation.core.tween
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material.icons.filled.Favorite
//import androidx.compose.material.icons.filled.FavoriteBorder
//import androidx.compose.material.icons.filled.KeyboardArrowDown
//import androidx.compose.material.icons.filled.KeyboardArrowUp
//import androidx.compose.material.icons.filled.Refresh
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.zenvest.stockchart.StockViewModel
//import com.example.zenvest.ui.StockScreenContent
//import com.example.zenvest.watchlist.WatchlistItem
//import com.example.zenvest.watchlist.WatchlistViewModel
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import okhttp3.Dispatcher
//import androidx.compose.ui.graphics.Color as ComposeColor
//
////
////// Color Palette
//object AppColors {
//    val Background = ComposeColor(0xFF0F0F1F)
//    val CardBackground = ComposeColor(0xFF1C1C2E)
//    val AccentGreen = ComposeColor(0xFF00FF66)
//    val AccentRed = ComposeColor(0xFFFF3D3D)
//    val AccentPurple = ComposeColor(0xFF7C3AED)
//    val TextPrimary = ComposeColor.White
//    val TextSecondary = ComposeColor(0xFFBBBBBB)
//    val TextTertiary = ComposeColor(0xFF6B7280)
//    val ChartBackground = ComposeColor(0xFF0B0C1C)
//    val Divider = ComposeColor(0xFF3B3B5B)
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CompanyOverviewScreen(
//    symbol: String,
//    onBack: () -> Unit,
//    stockprice: String? = null,
//    changePercentage: String? = null,
//    watchlistViewModel: WatchlistViewModel = viewModel(),
//) {
//    val companyViewModel: CompanyViewmodel = viewModel()
//    val stockViewModel: StockViewModel = viewModel()
//    val overview by companyViewModel.overview.collectAsState()
//    val stockPrice by stockViewModel.latestPrice.collectAsState()
//    var currentPrice by remember { mutableStateOf(stockprice) }
//    val isInWatchlist by watchlistViewModel.isInWatchlistFlow(symbol)
//        .collectAsState(initial = false)
//
//    LaunchedEffect(symbol) {
//        companyViewModel.loadCompany(symbol)
//        stockViewModel.fetchAllData(symbol)
//        overview?.price?.let { if (currentPrice == null) currentPrice = it }
//        stockPrice?.let { currentPrice = it }
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(
//                        "Company Overview",
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 22.sp,
//                        color = AppColors.TextPrimary
//                    )
//                },
//                navigationIcon = {
//                    IconButton(onClick = onBack) {
//                        Icon(
//                            Icons.Default.ArrowBack,
//                            contentDescription = "Back",
//                            tint = AppColors.TextPrimary
//                        )
//                    }
//                },
//                actions = {
//                    IconButton(onClick = {
//                        companyViewModel.loadCompany(symbol)
//                        stockViewModel.fetchAllData(symbol)
//                    }) {
//                        Icon(
//                            Icons.Default.Refresh,
//                            contentDescription = "Refresh",
//                            tint = AppColors.AccentGreen
//                        )
//                    }
//                },
//                colors = TopAppBarDefaults.mediumTopAppBarColors(
//                    containerColor = AppColors.CardBackground,
//                    scrolledContainerColor = AppColors.CardBackground,
//                    titleContentColor = AppColors.TextPrimary,
//                    navigationIconContentColor = AppColors.TextPrimary,
//                    actionIconContentColor = AppColors.AccentGreen
//                ),
//                modifier = Modifier.shadow(4.dp)
//            )
//
//        },
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = {
//                    overview?.let { company ->
//                        CoroutineScope(Dispatchers.Main).launch {
//                            watchlistViewModel.toggleWatchlist(
//                                WatchlistItem(
//                                    symbol = symbol,
//                                    name = company.name ?: "N/A",
//                                    price = currentPrice ?: company.price ?: "N/A",
//                                    changePercentage = changePercentage
//                                )
//                            )
//                        }
//                    }
//                },
//                containerColor = if (isInWatchlist) AppColors.AccentRed else AppColors.AccentGreen
//            ) {
//                Icon(
//                    imageVector = if (isInWatchlist) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
//                    contentDescription = "Watchlist"
//                )
//            }
//        },
//        containerColor = AppColors.Background
//    ) { padding ->
//        Column(
//            modifier = Modifier
//                .padding(padding)
//                .padding(horizontal = 16.dp)
//                .fillMaxSize()
//                .verticalScroll(rememberScrollState())
//                .padding(bottom = 24.dp)
//        ) {
//            overview?.let { company ->
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 16.dp),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Column {
//                        Text(
//                            text = symbol,
//                            style = MaterialTheme.typography.titleLarge,
//                            fontWeight = FontWeight.Bold,
//                            color = AppColors.TextPrimary
//                        )
//                        Text(
//                            text = company.name ?: "N/A",
//                            style = MaterialTheme.typography.bodyLarge,
//                            color = AppColors.TextSecondary
//                        )
//                    }
//                    Column(horizontalAlignment = Alignment.End) {
//                        Text(
//                            text = "$${currentPrice ?: company.price ?: "N/A"}",
//                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 22.sp),
//                            fontWeight = FontWeight.Bold,
//                            color = AppColors.TextPrimary
//                        )
//                        val isPositive = changePercentage?.contains("-") == false
//                        val arrowIcon = if (isPositive) "▲" else "▼"
//                        Text(
//                            text = "$arrowIcon $changePercentage",
//                            color = if (isPositive) AppColors.AccentGreen else AppColors.AccentRed,
//                            style = MaterialTheme.typography.bodyMedium,
//                            fontWeight = FontWeight.SemiBold
//                        )
//                    }
//                }
//                Spacer(modifier = Modifier.height(16.dp))
//                StockScreenContent(
//                    symbol = symbol,
//                    viewModel = stockViewModel,
//                    modifier = Modifier.fillMaxWidth()
//                )
//                Spacer(modifier = Modifier.height(24.dp))
//                CompanyOverviewContent(
//                    company = company.copy(price = currentPrice ?: company.price),
//                    isDummyData = company == dummyCompanyOverview,
//                    symbol = symbol,
//                    changePercentage = changePercentage
//                )
//            } ?: Column(
//                horizontalAlignment = Alignment.CenterHorizontally,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(24.dp)
//            ) {
//                Text(
//                    text = "Loading data...",
//                    color = AppColors.TextSecondary,
//                    style = MaterialTheme.typography.bodyLarge
//                )
//                Spacer(modifier = Modifier.height(12.dp))
//                CircularProgressIndicator(
//                    color = AppColors.AccentPurple,
//                    strokeWidth = 4.dp
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun CompanyOverviewContent(
//    company: UiCompanyOverview,
//    isDummyData: Boolean,
//    symbol: String,
//    changePercentage: String?
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clip(RoundedCornerShape(16.dp))
//            .background(AppColors.CardBackground)
//            .padding(24.dp)
//    ) {
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(
//                text = "About ${company.name ?: "N/A"}",
//                style = MaterialTheme.typography.headlineSmall,
//                fontWeight = FontWeight.Bold,
//                color = AppColors.TextPrimary
//            )
//            if (isDummyData) {
//                Text(
//                    text = "Sample Data",
//                    style = MaterialTheme.typography.labelSmall,
//                    color = AppColors.TextTertiary,
//                    modifier = Modifier
//                        .background(AppColors.Divider, RoundedCornerShape(8.dp))
//                        .padding(horizontal = 12.dp, vertical = 6.dp)
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        var expanded by remember { mutableStateOf(false) }
//        Column(
//            modifier = Modifier.animateContentSize(tween(300))
//        ) {
//            company.description?.let {
//
//                Text(
//                    text = if (expanded) it else it.take(100) + if (it.length > 100) "..." else "",
//                    style = MaterialTheme.typography.bodyLarge,
//                    color = AppColors.TextSecondary,
//                    maxLines = if (expanded) Int.MAX_VALUE else 3,
//                    overflow = TextOverflow.Ellipsis,
//                    modifier = Modifier.clickable { expanded = !expanded }
//                )
//                Text(
//                    text = if (expanded) "Read Less" else "Read More",
//                    modifier = Modifier
//                        .align(Alignment.End)
//                        .clickable { expanded = !expanded }
//                        .padding(top = 8.dp),
//                    color = AppColors.AccentPurple,
//                    fontSize = 14.sp,
//                    fontWeight = FontWeight.SemiBold
//                )
//
//            } ?: Text(
//                text = "No description available",
//                style = MaterialTheme.typography.bodyLarge,
//                color = AppColors.TextSecondary
//            )
//        }
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        company.industry?.let { Chip(text = "Industry: $it") }
//        Spacer(modifier = Modifier.height(12.dp))
//        company.sector?.let { Chip(text = "Sector: $it") }
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        PriceSliderDisplay(
//            week52Low = company.week52Low,
//            currentPrice = company.price,
//            week52High = company.week52High
//        )
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        MetricsGrid(company)
//    }
//}
//
//@Composable
//fun Chip(text: String) {
//    Box(
//        modifier = Modifier
////            .background(AppColors.Divider, RoundedCornerShape(50))
////            .border(1.dp, AppColors.TextTertiary, RoundedCornerShape(50))
////            .padding(horizontal = 16.dp, vertical = 10.dp)
////            .clickable { /* Optional: Add interaction feedback */ }
//            .clip(RoundedCornerShape(50))
//            .background(AppColors.Divider)
//            .padding(horizontal = 16.dp, vertical = 10.dp)
//    ) {
//        Text(
//            text = text,
//            style = MaterialTheme.typography.labelLarge,
//            color = AppColors.TextSecondary,
//            fontWeight = FontWeight.Medium
//        )
//    }
//}
//
//@Composable
//fun PriceSliderDisplay(week52Low: String?, currentPrice: String?, week52High: String?) {
//    val low = week52Low?.toFloatOrNull() ?: 0f
//    val current = currentPrice?.toFloatOrNull() ?: 0f
//    val high = week52High?.toFloatOrNull() ?: 1f
//    val position = if (high != low && currentPrice != null && currentPrice != "N/A") {
//        ((current - low) / (high - low)).coerceIn(0f, 1f)
//    } else {
//        0f
//    }
//
//    Column {
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(
//                text = "Current: $${currentPrice ?: "N/A"}",
//                style = MaterialTheme.typography.titleMedium,
//                fontWeight = FontWeight.Bold,
//                color = AppColors.TextPrimary
//            )
//            Text(
//                text = "52-Week Range",
//                style = MaterialTheme.typography.labelMedium,
//                color = AppColors.TextTertiary
//            )
//        }
//
//        Spacer(modifier = Modifier.height(12.dp))
//
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(8.dp)
//                .clip(RoundedCornerShape(4.dp))
//                .background(AppColors.Divider)
//        ) {
//            Box(
//                modifier = Modifier
//                    .fillMaxHeight()
//                    .fillMaxWidth(position)
//                    .background(
//                        brush = Brush.linearGradient(
//                            colors = listOf(AppColors.AccentPurple, AppColors.AccentGreen)
//                        ),
//                        shape = RoundedCornerShape(topStart = 6.dp, bottomStart = 6.dp)
//                    )
//            )
//            Box(
//                modifier = Modifier
//                    .fillMaxHeight()
//                    .fillMaxWidth(position)
//                    .padding(end = 4.dp),
//                contentAlignment = Alignment.CenterEnd
//            ) {
//                Box(
//                    modifier = Modifier
//                        .size(16.dp)
//                        .background(AppColors.TextPrimary, CircleShape)
//                        .border(2.dp, AppColors.AccentPurple, CircleShape)
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.height(12.dp))
//
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Text(
//                text = "$${week52Low ?: "N/A"}",
//                style = MaterialTheme.typography.labelLarge,
//                color = AppColors.TextTertiary
//            )
//            Text(
//                text = "$${week52High ?: "N/A"}",
//                style = MaterialTheme.typography.labelLarge,
//                color = AppColors.TextTertiary
//            )
//        }
//    }
//}
//
//@Composable
//fun MetricsGrid(company: UiCompanyOverview) {
//    Box(
//        modifier = Modifier
//            .clip(RoundedCornerShape(16.dp))
//            .background(AppColors.CardBackground)
//            .shadow(3.dp, RoundedCornerShape(16.dp))
//            .padding(16.dp)
//    )
//
//    Column(
//        verticalArrangement = Arrangement.spacedBy(16.dp),
//        modifier = Modifier.animateContentSize(tween(300))
//    ) {
//        Text(
//            text = "Key Metrics",
//            style = MaterialTheme.typography.headlineSmall,
//            fontWeight = FontWeight.Bold,
//            color = AppColors.TextPrimary
//        )
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            MetricItem(
//                label = "Market Cap",
//                value = company.marketCapitalization,
//                modifier = Modifier.weight(1f)
//            )
//            MetricItem(
//                label = "P/E Ratio",
//                value = company.peRatio,
//                modifier = Modifier.weight(1f)
//            )
//        }
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            MetricItem(
//                label = "PEG Ratio",
//                value = company.pegRatio,
//                modifier = Modifier.weight(1f)
//            )
//            MetricItem(
//                label = "Beta",
//                value = company.beta,
//                modifier = Modifier.weight(1f)
//            )
//        }
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            MetricItem(
//                label = "Dividend Yield",
//                value = company.dividendYield,
//                modifier = Modifier.weight(1f)
//            )
//            MetricItem(
//                label = "Profit Margin",
//                value = company.profitMargin,
//                modifier = Modifier.weight(1f)
//            )
//        }
//    }
//}
//
//@Composable
//fun MetricItem(label: String, value: String?, modifier: Modifier = Modifier) {
//    Box(
//        modifier = modifier
//            .clip(RoundedCornerShape(12.dp))
//            .background(AppColors.Divider)
//            .padding(16.dp)
//    ) {
//        Column {
//            Text(
//                text = label,
//                style = MaterialTheme.typography.labelLarge,
//                color = AppColors.TextTertiary,
//                fontWeight = FontWeight.Medium
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//            Text(
//                text = value ?: "N/A",
//                style = MaterialTheme.typography.titleMedium,
//                fontWeight = FontWeight.SemiBold,
//                color = AppColors.TextPrimary
//            )
//        }
//    }
//}
//
//val dummyCompanyOverview = UiCompanyOverview(
//    name = "TechCorp Inc.",
//    description = "TechCorp Inc. is a leading technology company specializing in innovative software solutions and cloud computing services. Founded in 2005, the company has grown to become a major player in the tech industry, offering a wide range of products including enterprise software, AI-powered analytics, and cybersecurity solutions. With a global presence in over 50 countries, TechCorp is committed to driving digital transformation for businesses worldwide.",
//    sector = "Technology",
//    industry = "Services-Computer Programming, Data Processing, Etc.",
//    marketCapitalization = "11472100",
//    peRatio = "None",
//    pegRatio = "None",
//    dividendYield = "None",
//    profitMargin = "-0.581",
//    beta = "1.598",
//    week52High = "$10.54",
//    week52Low = "$2.28",
//    price = "8.3"
//)
//
//@Preview(showBackground = true)
//@Composable
//fun CompanyOverviewScreenPreview() {
//    MaterialTheme {
//        CompanyOverviewScreen(
//            symbol = "TECH",
//            onBack = {},
//            stockprice = "8.3",
//            changePercentage = "2.34%"
//        )
//    }
//}
//

package com.example.zenvest.companyOverview

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.zenvest.stockchart.StockViewModel
import com.example.zenvest.ui.StockScreenContent
import com.example.zenvest.watchlist.AppDatabase
import com.example.zenvest.watchlist.WatchlistItem
import com.example.zenvest.watchlist.WatchlistRepository
import com.example.zenvest.watchlist.WatchlistViewModel
import com.example.zenvest.watchlist.WatchlistViewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

// Color Palette
object AppColors {
    val Background = androidx.compose.ui.graphics.Color(0xFF0F0F1F)
    val CardBackground = androidx.compose.ui.graphics.Color(0xFF1C1C2E)
    val AccentGreen = androidx.compose.ui.graphics.Color(0xFF00FF66)
    val AccentRed = androidx.compose.ui.graphics.Color(0xFFFF3D3D)
    val AccentPurple = androidx.compose.ui.graphics.Color(0xFF7C3AED)
    val TextPrimary = androidx.compose.ui.graphics.Color.White
    val TextSecondary = androidx.compose.ui.graphics.Color(0xFFBBBBBB)
    val TextTertiary = androidx.compose.ui.graphics.Color(0xFF6B7280)
    val ChartBackground = androidx.compose.ui.graphics.Color(0xFF0B0C1C)
    val Divider = androidx.compose.ui.graphics.Color(0xFF3B3B5B)
}

// Data class for company overview (based on dummyCompanyOverview)

// Placeholder CompanyViewModel (replace with actual implementation)
class CompanyViewModel : ViewModel() {
    val overview = MutableStateFlow<UiCompanyOverview?>(null)
    suspend fun loadCompany(symbol: String) {
        // TODO: Replace with actual API call to fetch company data
        overview.value = dummyCompanyOverview
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyOverviewScreen(
    symbol: String,
    stockPrice: String,
    changePercentage: String,
    onBack: () -> Unit,
    watchlistViewModel: WatchlistViewModel = viewModel(
        factory = WatchlistViewModelFactory(
            WatchlistRepository(AppDatabase.getDatabase(LocalContext.current).watchlistDao())
        )
    ),
    companyViewModel: CompanyViewModel = viewModel(),
    stockViewModel: StockViewModel = viewModel()
) {
    val overview by companyViewModel.overview.collectAsState()
    val stockPriceState by stockViewModel.latestPrice.collectAsState()
    var currentPrice by remember { mutableStateOf(stockPrice) }
    val isInWatchlist by watchlistViewModel.isInWatchlistFlow(symbol).collectAsState(initial = false)
    val scope = rememberCoroutineScope()

    // Load company and stock data, update currentPrice
    LaunchedEffect(symbol) {
        companyViewModel.loadCompany(symbol)
        stockViewModel.fetchAllData(symbol)
        currentPrice = stockPriceState ?: stockPrice.takeIf { it != "N/A" } ?: overview?.price ?: "N/A"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Company Overview",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = AppColors.TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = AppColors.TextPrimary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        scope.launch {
                            companyViewModel.loadCompany(symbol)
                            stockViewModel.fetchAllData(symbol)
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = AppColors.AccentGreen
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppColors.CardBackground,
                    titleContentColor = AppColors.TextPrimary,
                    navigationIconContentColor = AppColors.TextPrimary,
                    actionIconContentColor = AppColors.AccentGreen
                ),
                modifier = Modifier.shadow(4.dp)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    scope.launch {
                        overview?.let { company ->
                            watchlistViewModel.toggleWatchlist(
                                WatchlistItem(
                                    symbol = symbol,
                                    name = company.name ?: "N/A",
                                    price = currentPrice,
                                    changePercentage = changePercentage
                                )
                            )
                        }
                    }
                },
                containerColor = if (isInWatchlist) AppColors.AccentRed else AppColors.AccentGreen
            ) {
                Icon(
                    imageVector = if (isInWatchlist) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Toggle Watchlist"
                )
            }
        },
        containerColor = AppColors.Background
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 24.dp)
        ) {
            overview?.let { company ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = symbol,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = AppColors.TextPrimary
                        )
                        Text(
                            text = company.name ?: "N/A",
                            style = MaterialTheme.typography.bodyLarge,
                            color = AppColors.TextSecondary
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "$$currentPrice",
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 22.sp),
                            fontWeight = FontWeight.Bold,
                            color = AppColors.TextPrimary
                        )
                        val isPositive = changePercentage.let { it != "N/A" && !it.contains("-") }
                        val arrowIcon = if (isPositive) "▲" else "▼"
                        Text(
                            text = "$arrowIcon $changePercentage",
                            color = if (isPositive) AppColors.AccentGreen else AppColors.AccentRed,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                StockScreenContent(
                    symbol = symbol,
                    viewModel = stockViewModel,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))
                CompanyOverviewContent(
                    company = company.copy(price = currentPrice),
                    isDummyData = company == dummyCompanyOverview,
                    symbol = symbol,
                    changePercentage = changePercentage
                )
            } ?: Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = "Loading data...",
                    color = AppColors.TextSecondary,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(12.dp))
                CircularProgressIndicator(
                    color = AppColors.AccentPurple,
                    strokeWidth = 4.dp
                )
                Spacer(modifier = Modifier.height(12.dp))
                TextButton(onClick = {
                    scope.launch {
                        companyViewModel.loadCompany(symbol)
                        stockViewModel.fetchAllData(symbol)
                    }
                }) {
                    Text(text = "Retry", color = AppColors.AccentPurple)
                }
            }
        }
    }
}

@Composable
fun CompanyOverviewContent(
    company: UiCompanyOverview,
    isDummyData: Boolean,
    symbol: String,
    changePercentage: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(AppColors.CardBackground)
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "About ${company.name ?: "N/A"}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextPrimary
            )
            if (isDummyData) {
                Text(
                    text = "Sample Data",
                    style = MaterialTheme.typography.labelSmall,
                    color = AppColors.TextTertiary,
                    modifier = Modifier
                        .background(AppColors.Divider, RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        var expanded by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier.animateContentSize(tween(300))
        ) {
            company.description?.let {
                Text(
                    text = if (expanded) it else it.take(100) + if (it.length > 100) "..." else "",
                    style = MaterialTheme.typography.bodyLarge,
                    color = AppColors.TextSecondary,
                    maxLines = if (expanded) Int.MAX_VALUE else 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.clickable { expanded = !expanded }
                )
                Text(
                    text = if (expanded) "Read Less" else "Read More",
                    modifier = Modifier
                        .align(Alignment.End)
                        .clickable { expanded = !expanded }
                        .padding(top = 8.dp),
                    color = AppColors.AccentPurple,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            } ?: Text(
                text = "No description available",
                style = MaterialTheme.typography.bodyLarge,
                color = AppColors.TextSecondary
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        company.industry?.let { Chip(text = "Industry: $it") }
        Spacer(modifier = Modifier.height(12.dp))
        company.sector?.let { Chip(text = "Sector: $it") }

        Spacer(modifier = Modifier.height(24.dp))

        PriceSliderDisplay(
            week52Low = company.week52Low,
            currentPrice = company.price,
            week52High = company.week52High
        )

        Spacer(modifier = Modifier.height(24.dp))

        MetricsGrid(company)
    }
}

@Composable
fun Chip(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(AppColors.Divider)
            .border(1.dp, AppColors.TextTertiary, RoundedCornerShape(50))
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .clickable { /* Add interaction feedback if needed */ }
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = AppColors.TextSecondary,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun PriceSliderDisplay(
    week52Low: String?,
    currentPrice: String?,
    week52High: String?
) {
    val low = week52Low?.toFloatOrNull() ?: 0f
    val current = currentPrice?.toFloatOrNull() ?: 0f
    val high = week52High?.toFloatOrNull() ?: 1f
    val position = if (high != low && currentPrice != null && currentPrice != "N/A") {
        ((current - low) / (high - low)).coerceIn(0f, 1f)
    } else {
        0f
    }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Current: $${currentPrice ?: "N/A"}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextPrimary
            )
            Text(
                text = "52-Week Range",
                style = MaterialTheme.typography.labelMedium,
                color = AppColors.TextTertiary
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(AppColors.Divider)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(position)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(AppColors.AccentPurple, AppColors.AccentGreen)
                        ),
                        shape = RoundedCornerShape(topStart = 6.dp, bottomStart = 6.dp)
                    )
            )
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(position)
                    .padding(end = 4.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(AppColors.TextPrimary, CircleShape)
                        .border(2.dp, AppColors.AccentPurple, CircleShape)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "$${week52Low ?: "N/A"}",
                style = MaterialTheme.typography.labelLarge,
                color = AppColors.TextTertiary
            )
            Text(
                text = "$${week52High ?: "N/A"}",
                style = MaterialTheme.typography.labelLarge,
                color = AppColors.TextTertiary
            )
        }
    }
}

@Composable
fun MetricsGrid(company: UiCompanyOverview) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(AppColors.CardBackground)
            .shadow(3.dp, RoundedCornerShape(16.dp))
            .padding(16.dp)
            .animateContentSize(tween(300))
    ) {
        Text(
            text = "Key Metrics",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = AppColors.TextPrimary
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MetricItem(
                label = "Market Cap",
                value = company.marketCapitalization,
                modifier = Modifier.weight(1f)
            )
            MetricItem(
                label = "P/E Ratio",
                value = company.peRatio,
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MetricItem(
                label = "PEG Ratio",
                value = company.pegRatio,
                modifier = Modifier.weight(1f)
            )
            MetricItem(
                label = "Beta",
                value = company.beta,
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MetricItem(
                label = "Dividend Yield",
                value = company.dividendYield,
                modifier = Modifier.weight(1f)
            )
            MetricItem(
                label = "Profit Margin",
                value = company.profitMargin,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun MetricItem(
    label: String,
    value: String?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(AppColors.Divider)
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = AppColors.TextTertiary,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value ?: "N/A",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = AppColors.TextPrimary
            )
        }
    }
}

val dummyCompanyOverview = UiCompanyOverview(
    name = "TechCorp Inc.",
    description = "TechCorp Inc. is a leading technology company specializing in innovative software solutions and cloud computing services. Founded in 2005, the company has grown to become a major player in the tech industry, offering a wide range of products including enterprise software, AI-powered analytics, and cybersecurity solutions. With a global presence in over 50 countries, TechCorp is committed to driving digital transformation for businesses worldwide.",
    sector = "Technology",
    industry = "Services-Computer Programming, Data Processing, Etc.",
    marketCapitalization = "11472100",
    peRatio = "None",
    pegRatio = "None",
    dividendYield = "None",
    profitMargin = "-0.581",
    beta = "1.598",
    week52High = "10.54",
    week52Low = "2.28",
    price = "8.3"
)

@Preview(showBackground = true)
@Composable
fun CompanyOverviewScreenPreview() {
    MaterialTheme {
        CompanyOverviewScreen(
            symbol = "TECH",
            stockPrice = "8.3",
            changePercentage = "2.34%",
            onBack = {}
        )
    }
}
