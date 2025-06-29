package com.example.zenvest


import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.zenvest.companyOverview.CompanyOverviewScreen
import com.example.zenvest.stockinfo.StockScreen
import com.example.zenvest.topmovers.FullStockListScreen
import com.example.zenvest.topmovers.TopMoversScreen
import com.example.zenvest.topmovers.TopMoversViewModel

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    viewModel: TopMoversViewModel = viewModel()

) {
    val state = viewModel.topMoversState
    NavHost(navController = navController, startDestination = "top_movers") {
        composable("stock") {
            StockScreen()
            //TopMoversScreen()
        }
        composable("graph") {
            // GraphScreen()
        }
        // Add more screens here
        composable("top_movers") {
            TopMoversScreen(
                viewModel = viewModel,
                onViewAllClick = { category -> navController.navigate("full_list/$category") },
                onStockClick = { symbol -> navController.navigate("company_overview/$symbol") }
            )
        }
        composable(
            route = "full_list/{category}",
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
                title = category,
                stocks = stocks,
                onBack = { navController.popBackStack() },
                onStockClick = { symbol -> navController.navigate("company_overview/$symbol") }
            )
        }

        composable(
            "company_overview/{symbol}",
            arguments = listOf(navArgument("symbol") { type = NavType.StringType })
        ) { backStackEntry ->
            val symbol = backStackEntry.arguments?.getString("symbol") ?: ""
            CompanyOverviewScreen(symbol = symbol) {
                navController.popBackStack()
            }
        }

    }
}