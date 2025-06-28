package com.example.zenvest.topmovers

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zenvest.api.RetrofitInstance
import com.example.zenvest.api.TopMoversResponse
import kotlinx.coroutines.launch
import javax.inject.Inject

class TopMoversViewModel @Inject constructor(): ViewModel() {
    var topMoversState by mutableStateOf<TopMoversResponse?>(null)
    var errorMessage by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(false)

    fun fetchTopMovers(apiKey: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = RetrofitInstance.api.getTopMovers(apiKey = apiKey)
                if (response.isSuccessful) {
                    topMoversState = response.body()
                    errorMessage = null
                } else {
                    errorMessage = "Failed: ${response.message()}"
                }
            } catch (e: Exception) {
                errorMessage = e.message
            }
            isLoading = false
        }
    }
}