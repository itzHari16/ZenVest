package com.example.zenvest.stockinfo


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zenvest.api.StockRepository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StockViewModel : ViewModel() {
    private val repository = StockRepository()

    private val _stockPrice = MutableStateFlow<Double?>(null)
    val stockPrice: StateFlow<Double?> = _stockPrice

    private val _companyName = MutableStateFlow("")
    val companyName: StateFlow<String> = _companyName

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchStock(symbol: String) {
        viewModelScope.launch {
            try {
                val price = repository.fetchStockPrice(symbol)
                val name = repository.fetchCompanyName(symbol)
                _stockPrice.value = price
                _companyName.value = name
            } catch (e: Exception) {
                _error.value = e.localizedMessage
            }
        }
    }
}