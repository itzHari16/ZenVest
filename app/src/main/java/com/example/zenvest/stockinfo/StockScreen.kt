package com.example.zenvest.stockinfo
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

@Composable
fun StockPriceScreen() {
    val viewModel: StockViewModel = viewModel()
    val stockPrice by viewModel.stockPrice.collectAsState()
    val companyName by viewModel.companyName.collectAsState()
    val error by viewModel.error.collectAsState()


    var input by remember { mutableStateOf(TextFieldValue("")) }
    var previousPrice by remember { mutableStateOf(0.0) }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            label = { Text("Stock Symbol") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = {
            viewModel.fetchStock(input.text.trim())
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Fetch Stock Info")
        }

        Spacer(modifier = Modifier.height(20.dp))

        stockPrice?.let { price ->
            val priceChange = price - previousPrice
            val percentageChange = if (previousPrice != 0.0) (priceChange / previousPrice) * 100 else 0.0
            previousPrice = price

            Text("Price: $${String.format("%.2f", price)}", color = if (priceChange >= 0) Color.Green else Color.Red)
            Text("Change: ${String.format("%.2f", percentageChange)}%")
        }

        if (companyName.isNotBlank()) {
            Text("Company: $companyName")
        }

        error?.let {
            Text("Error: $it", color = Color.Red)
        }
        Spacer(modifier = Modifier.height(50.dp))
    }
}