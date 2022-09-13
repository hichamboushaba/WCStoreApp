package com.hicham.wcstoreapp.ui.main

import com.hicham.wcstoreapp.android.data.cart.CartRepository
import com.hicham.wcstoreapp.android.data.cart.items
import com.hicham.wcstoreapp.ui.BaseViewModel
import com.hicham.wcstoreapp.ui.NavigationManager
import com.hicham.wcstoreapp.ui.navigation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class MainViewModel(cartRepository: CartRepository, val navigationManager: NavigationManager) : BaseViewModel() {
    val uiState = cartRepository.items
        .map { list ->
            UiState(countOfItemsInCart = list.sumOf { it.quantity })
        }
        .toStateFlow(UiState(0))

    data class UiState(
        val countOfItemsInCart: Int
    )

    fun onCartButtonClick() {
        navigationManager.navigate(Screen.Cart.route)
    }
}