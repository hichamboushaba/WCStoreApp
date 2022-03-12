package com.hicham.wcstoreapp.android.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hicham.wcstoreapp.android.data.cart.CartRepository
import com.hicham.wcstoreapp.android.ui.navigation.AndroidNavigationManager
import com.hicham.wcstoreapp.data.currencyformat.CurrencyFormatProvider
import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.ui.CurrencyFormatter
import com.hicham.wcstoreapp.ui.navigation.Screen
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CartViewModel(
    private val cartRepository: CartRepository,
    private val currencyFormatProvider: CurrencyFormatProvider,
    private val navigationManager: AndroidNavigationManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(CartUiState())
    val uiState = _uiState.asStateFlow()

    init {
        combine(
            cartRepository.cart,
            currencyFormatProvider.formatSettings
        ) { cart, formatSettings ->
            val currencyFormatter = CurrencyFormatter(formatSettings)
            val items = cart.items.map {
                CartItemUiModel(
                    product = it.product,
                    quantity = it.quantity,
                    totalPriceFormatted = currencyFormatter.format(it.totals.subtotal)
                )
            }
            _uiState.update {
                it.copy(
                    cartItems = items,
                    subtotalFormatted = currencyFormatter.format(cart.totals.subtotal),
                    taxFormatted = currencyFormatter.format(cart.totals.tax),
                    // Since we don't show shipping cost here, let's calculate the total manually
                    totalFormatted = currencyFormatter.format(cart.totals.subtotal + cart.totals.tax)
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onIncreaseQuantity(product: Product) {
        viewModelScope.launch {
            _uiState.update { it.copy(isUpdatingCart = true) }
            cartRepository.addItem(product)
            _uiState.update { it.copy(isUpdatingCart = false) }
        }
    }

    fun onDecreaseQuantity(product: Product) {
        viewModelScope.launch {
            _uiState.update { it.copy(isUpdatingCart = true) }
            cartRepository.deleteItem(product)
            _uiState.update { it.copy(isUpdatingCart = false) }
        }
    }

    fun onRemoveProduct(product: Product) {
        viewModelScope.launch {
            _uiState.update { it.copy(isUpdatingCart = true) }
            cartRepository.clearProduct(product)
            _uiState.update { it.copy(isUpdatingCart = false) }
        }
    }

    fun onBackClicked() {
        navigationManager.navigateUp()
    }

    fun onGoToProductsClicked() {
        navigationManager.popUpTo(Screen.Home.route)
    }

    fun onCheckoutClicked() {
        navigationManager.navigate(Screen.Checkout.route)
    }

    data class CartUiState(
        val isUpdatingCart: Boolean = false,
        val cartItems: List<CartItemUiModel> = emptyList(),
        val subtotalFormatted: String = "",
        val taxFormatted: String = "",
        val totalFormatted: String = ""
    )

    data class CartItemUiModel(
        val product: Product,
        val quantity: Int,
        val totalPriceFormatted: String
    )
}