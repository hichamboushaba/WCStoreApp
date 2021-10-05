package com.hicham.wcstoreapp.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hicham.wcstoreapp.data.cart.CartRepository
import com.hicham.wcstoreapp.data.currencyformat.CurrencyFormatProvider
import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.ui.CurrencyFormatter
import com.hicham.wcstoreapp.ui.navigation.NavigationManager
import com.hicham.wcstoreapp.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val currencyFormatProvider: CurrencyFormatProvider,
    private val navigationManager: NavigationManager
) : ViewModel() {
    val uiState: Flow<CartUiState>

    init {
        uiState = combine(
            cartRepository.items,
            currencyFormatProvider.formatSettings
        ) { cartItems, formatSettings ->
            val currencyFormatter = CurrencyFormatter(formatSettings)
            val items = cartItems.map {
                CartItemUiModel(
                    product = it.product,
                    quantity = it.quantity,
                    totalPriceFormatted = currencyFormatter.format(it.product.price.multiply(it.quantity.toBigDecimal()))
                )
            }
            val totalPrice = cartItems.sumOf { it.product.price * it.quantity.toBigDecimal() }
            CartUiState(
                cartItems = items,
                subtotalFormatted = currencyFormatter.format(totalPrice),
                totalFormatted = currencyFormatter.format(totalPrice)
            )
        }
            .flowOn(Dispatchers.Default)
            .shareIn(viewModelScope, started = SharingStarted.Lazily, replay = 1)
    }

    fun onIncreaseQuantity(product: Product) {
        viewModelScope.launch {
            cartRepository.addItem(product)
        }
    }

    fun onDecreaseQuantity(product: Product) {
        viewModelScope.launch {
            cartRepository.deleteItem(product)
        }
    }

    fun onRemoveProduct(product: Product) {
        viewModelScope.launch {
            cartRepository.clearProduct(product)
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
        val cartItems: List<CartItemUiModel> = emptyList(),
        val subtotalFormatted: String = "",
        val totalFormatted: String = ""
    ) {
        val shippingCost: String = "Free" // TODO
    }

    data class CartItemUiModel(
        val product: Product,
        val quantity: Int,
        val totalPriceFormatted: String
    )
}