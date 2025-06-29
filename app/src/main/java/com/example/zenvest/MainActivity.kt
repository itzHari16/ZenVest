package com.example.zenvest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.stockapp.ui.StockGraph
import com.example.zenvest.ui.theme.ZenVestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZenVestTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    NavGraph()
                    //StockGraph()
                }
            }
        }
    }
}

