package com.hicham.wcstoreapp.android.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hicham.wcstoreapp.android.data.cart.CartRepository
import com.hicham.wcstoreapp.android.data.cart.items
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class MainViewModel(cartRepository: CartRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState(0))
    val uiState = _uiState.asStateFlow()

    init {
        cartRepository.items
            .map { list -> list.sumOf { it.quantity } }
            .onEach { _uiState.update { state -> state.copy(countOfItemsInCart = it) } }
            .flowOn(Dispatchers.Default)
            .launchIn(viewModelScope)
    }

    data class UiState(
        val countOfItemsInCart: Int
    )
}