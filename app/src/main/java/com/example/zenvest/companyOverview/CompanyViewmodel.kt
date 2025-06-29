package com.example.zenvest.companyOverview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zenvest.api.CompanyOverview
import com.example.zenvest.api.StockRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CompanyViewmodel : ViewModel() {
    private val repository = StockRepository()

    private val _overview = MutableStateFlow<CompanyOverview?>(null)
    val overview: StateFlow<CompanyOverview?> = _overview

    fun loadCompany(symbol: String) {
        viewModelScope.launch {
            try {
                _overview.value = repository.fetchCompanyOverview(symbol)
            } catch (e: Exception) {
                _overview.value = null
            }
        }
    }
}