package com.example.zenvest.companyOverview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyOverviewScreen(symbol: String, onBack: () -> Unit) {
    val viewModel: CompanyViewmodel = viewModel()
    val overview by viewModel.overview.collectAsState()

    LaunchedEffect(symbol) {
        viewModel.loadCompany(symbol)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Company Overview") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                }
            )
        },
        containerColor = Color(0xFF0B0C1C)
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .background(Color(0xFF0B0C1C))
        ) {
            overview?.let {
                Text("Name: ${it.name}", color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Sector: ${it.sector}", color = Color.White)
                Text("Industry: ${it.industry}", color = Color.White)
                Text("Market Cap: ${it.marketCapitalization}", color = Color.White)
                Text("PE Ratio: ${it.peRatio}", color = Color.White)
                Text("Dividend Yield: ${it.dividendYield}", color = Color.White)
                Text("Profit Margin: ${it.profitMargin}", color = Color.White)
                Text("Beta: ${it.beta}", color = Color.White)
                Text("52W High: ${it.week52High}", color = Color.White)
                Text("52W Low: ${it.week52Low}", color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Description:\n${it.description}", color = Color.LightGray)
            } ?: Text("Loading or failed to load...", color = Color.Gray)
        }
    }
}
