package com.hicham.wcstoreapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hicham.wcstoreapp.data.Status
import com.hicham.wcstoreapp.data.source.ProductsRepository
import com.hicham.wcstoreapp.models.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: ProductsRepository) : ViewModel() {
    private var _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchProducts()
    }

    private fun fetchProducts() {
        repository.getProductList()
            .onStart {
                _uiState.update { state ->
                    state.copy(
                        isLoading = true
                    )
                }
            }
            .onEach {
                if (it.isSuccess) {
                    _uiState.update { state -> state.copy(products = it.getOrThrow()) }
                } else {
                    // TODO
                }
            }
            .onCompletion {
                _uiState.update { state ->
                    state.copy(isLoading = false)
                }
            }
            .launchIn(viewModelScope)
    }
}

data class UiState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList()
)