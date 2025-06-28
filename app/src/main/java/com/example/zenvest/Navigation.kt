package com.example.zenvest


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.zenvest.stockinfo.StockScreen
import com.example.zenvest.topmovers.TopMoversScreen

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = "stock") {
        composable("stock") {
            //StockScreen()
            TopMoversScreen()
        }
        // Add more screens here
    }
}