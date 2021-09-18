package com.hicham.wcstoreapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hicham.wcstoreapp.data.Cart
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(cart: Cart) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState(0))
    val uiState = _uiState.asStateFlow()

    init {
        cart.items
            .map { it.size }
            .onEach { _uiState.update { state -> state.copy(countOfItemsInCart = it) } }
            .launchIn(viewModelScope)
    }

    data class UiState(
        val countOfItemsInCart: Int
    )
}