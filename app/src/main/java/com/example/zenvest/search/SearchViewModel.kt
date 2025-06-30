package com.example.zenvest.stockchart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zenvest.api.RetrofitInstance
import com.example.zenvest.api.SymbolMatch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val apiKey: String = "53Q7L9RG7J5RCSJV"

    private val _searchResults = MutableStateFlow<List<SymbolMatch>>(emptyList())
    val searchResults: StateFlow<List<SymbolMatch>> = _searchResults

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun searchSymbols(keywords: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = RetrofitInstance.api.searchSymbols(keywords = keywords, apikey = apiKey)
                if (response.isSuccessful) {
                    response.body()?.bestMatches?.let { _searchResults.value = it }
                        ?: run { _error.value = "No search results found" }
                } else {
                    _error.value = "API error ${response.code()}: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Network error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}