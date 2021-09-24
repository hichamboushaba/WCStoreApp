package com.hicham.wcstoreapp.ui.cart

import androidx.lifecycle.ViewModel
import com.hicham.wcstoreapp.data.CartRepository
import com.hicham.wcstoreapp.data.CurrencyFormatProvider
import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.ui.CurrencyFormatter
import com.hicham.wcstoreapp.ui.navigation.NavigationManager
import com.hicham.wcstoreapp.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val currencyFormatProvider: CurrencyFormatProvider,
    private val navigationManager: NavigationManager
) : ViewModel() {
    val items =
        combine(
            cartRepository.items,
            currencyFormatProvider.formatSettings
        ) { cartItems, formatSettings ->
            Pair(cartItems, CurrencyFormatter(formatSettings))
        }.map { (cartItems, currencyFormatter) ->
            cartItems.map {
                CartItemUiModel(
                    product = it.product,
                    quantity = it.quantity,
                    totalPriceFormatted = currencyFormatter.format(it.product.price.multiply(it.quantity.toBigDecimal()))
                )
            }
        }

    fun onIncreaseQuantity(product: Product) {
        cartRepository.addItem(product)
    }

    fun onDecreaseQuantity(product: Product) {
        cartRepository.deleteItem(product)
    }

    fun onRemoveProduct(product: Product) {
        cartRepository.clearProduct(product)
    }

    fun onBackClicked() {
        navigationManager.navigateUp()
    }

    fun onGoToProductsClicked() {
        navigationManager.popUpTo(Screen.Home.route)
    }

    data class CartItemUiModel(
        val product: Product,
        val quantity: Int,
        val totalPriceFormatted: String
    )
}