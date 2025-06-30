package com.example.zenvest.companyOverview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zenvest.api.StockRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CompanyViewmodel : ViewModel() {
    private val repository = StockRepository()

    private val _overview = MutableStateFlow<UiCompanyOverview?>(null)
    val overview: StateFlow<UiCompanyOverview?> = _overview

    fun loadCompany(symbol: String) {
        viewModelScope.launch {
            try {
                // Fetch company overview from the repository
                val response = repository.getCompanyOverview(symbol)

                // Map API response to UI model
                _overview.value = UiCompanyOverview(
                    name = response.name ?: "N/A",
                    description = response.description ?: "No description available",
                    sector = response.sector ?: "N/A",
                    industry = response.industry ?: "N/A",
                    marketCapitalization = response.marketCapitalization ?: "N/A",
                    peRatio = response.peRatio ?: "N/A",
                    pegRatio = response.pegRatio ?: "N/A",
                    dividendYield = response.dividendYield ?: "N/A",
                    profitMargin = response.profitMargin ?: "N/A",
                    beta = response.beta ?: "N/A",
                    week52High = response.week52High ?: "N/A",
                    week52Low = response.week52Low ?: "N/A",
                    price = response.price ?: "N/A",
                )
            } catch (e: Exception) {
                // Set dummy data on failure
               // _overview.value = dummyCompanyOverview
                // Optionally, log the error for debugging
                // Log.e("CompanyViewModel", "Failed to fetch company overview: ${e.message}")
                
            }
        }
    }
}

// UI Model (same as in your UI code for consistency)
// UI Model
// UI Model (same as in your UI code for consistency)
data class UiCompanyOverview(
    val name: String?,
    val description: String?,
    val sector: String?,
    val industry: String?,
    val marketCapitalization: String?,
    val peRatio: String?,
    val pegRatio: String?, // Added PEGRatio
    val dividendYield: String?,
    val profitMargin: String?,
    val beta: String?,
    val week52High: String?,
    val week52Low: String?,
    val price: String?,
)

//public val dummyCompanyOverview = UiCompanyOverview(
//    name = "TechCorp Inc.",
//    description = "TechCorp Inc. is a leading technology company specializing in innovative software solutions and cloud computing services. Founded in 2005, the company has grown to become a major player in the tech industry, offering a wide range of products including enterprise software, AI-powered analytics, and cybersecurity solutions. With a global presence in over 50 countries, TechCorp is committed to driving digital transformation for businesses worldwide.",
//    sector = "Technology",
//    industry = "Software",
//    marketCapitalization = "$1.2T",
//    peRatio = "28.5",
//    pegRatio = "1.65", // Added dummy PEGRatio
//    dividendYield = "1.8%",
//    profitMargin = "22.3%",
//    beta = "1.15",
//    week52High = "145.50",
//    week52Low = "95.20",
//    price = "120.75",
//)