package com.example.zenvest


import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.zenvest.companyOverview.CompanyOverviewScreen
import com.example.zenvest.stockinfo.StockPriceScreen
import com.example.zenvest.topmovers.FullStockListScreen
import com.example.zenvest.topmovers.TopMoversScreen
import com.example.zenvest.topmovers.TopMoversViewModel
import com.example.zenvest.ui.SearchScreen
import com.example.zenvest.watchlist.AppDatabase
import com.example.zenvest.watchlist.WatchlistRepository
import com.example.zenvest.watchlist.WatchlistScreen
import com.example.zenvest.watchlist.WatchlistViewModelFactory

object Routes {
    const val TOP_MOVERS = "top_movers"
    const val FULL_LIST = "full_list/{category}"
    const val COMPANY_OVERVIEW = "company_overview/{symbol}?price={price}&changePercentage={changePercentage}"
    const val SEARCH = "search"
    const val STOCK_DETAIL = "stock_detail/{ticker}"
    const val WATCHLIST = "watchlist"
}

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    viewModel: TopMoversViewModel = viewModel()

) {
    val state = viewModel.topMoversState
    NavHost(navController = navController, startDestination = Routes.TOP_MOVERS) {
        composable(Routes.TOP_MOVERS) {
            TopMoversScreen(
                viewModel = viewModel,
                onViewAllClick = { category ->
                    navController.navigate("full_list/$category")
                },
                onStockClick = { symbol, price, changePercentage ->
                    navController.navigate(
                        "${Routes.COMPANY_OVERVIEW.replace("{symbol}", symbol)}" +
                                "?price=${price ?: "N/A"}" +
                                "&changePercentage=${changePercentage ?: "N/A"}"
                    )
                },
                onSearchClick = { navController.navigate(Routes.SEARCH) },
                onWatchlistClick = { navController.navigate(Routes.WATCHLIST) } // Added
            )
        }

        composable(
            route = Routes.FULL_LIST,
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            val stocks = when (category) {
                "gainers" -> state?.topGainers ?: emptyList()
                "losers" -> state?.topLosers ?: emptyList()
                "active" -> state?.mostActive ?: emptyList()
                else -> emptyList()
            }
            FullStockListScreen(
                title = category.replaceFirstChar { it.uppercase() },
                stocks = stocks,
                onBack = { navController.popBackStack() },
                onStockClick = { symbol, price, changePercentage ->
                    navController.navigate(
                        "${Routes.COMPANY_OVERVIEW.replace("{symbol}", symbol)}" +
                                "?price=${price ?: "N/A"}" +
                                "&changePercentage=${changePercentage ?: "N/A"}"
                    )
                }
            )
        }

        composable(
            route = Routes.COMPANY_OVERVIEW,
            arguments = listOf(
                navArgument("symbol") { type = NavType.StringType },
                navArgument("price") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = "N/A"
                },
                navArgument("changePercentage") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = "N/A"
                }
            )
        ) { backStackEntry ->
            val symbol = backStackEntry.arguments?.getString("symbol") ?: ""
            val price = backStackEntry.arguments?.getString("price") ?: "N/A"
            val changePercentage = backStackEntry.arguments?.getString("changePercentage") ?: "N/A"
            CompanyOverviewScreen(
                symbol = symbol,
                stockPrice = price, // Renamed for consistency
                changePercentage = changePercentage,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.SEARCH) {
            SearchScreen(
                onBack = { navController.popBackStack() },
                onStockClick = { ticker, _ -> // Ignore name for now
                    navController.navigate("${Routes.STOCK_DETAIL.replace("{ticker}", ticker)}")
                }
            )
        }

//        composable(
//            route = Routes.STOCK_DETAIL,
//            arguments = listOf(navArgument("ticker") { type = NavType.StringType })
//        ) { backStackEntry ->
//            val ticker = backStackEntry.arguments?.getString("ticker") ?: ""
//            StockPriceScreen(
//                symbol = ticker,
//                onBack = { navController.popBackStack() }
//            )
//        }

        composable(Routes.WATCHLIST) {
            WatchlistScreen(
                navController = navController,
                viewModel = viewModel(
                    factory = WatchlistViewModelFactory(
                        WatchlistRepository(AppDatabase.getDatabase(LocalContext.current).watchlistDao())
                    )
                )
            )
        }
    }
}