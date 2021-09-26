package com.hicham.wcstoreapp.ui.checkout

import androidx.lifecycle.viewModelScope
import com.hicham.wcstoreapp.data.CartRepository
import com.hicham.wcstoreapp.data.CurrencyFormatProvider
import com.hicham.wcstoreapp.models.Address
import com.hicham.wcstoreapp.ui.BaseViewModel
import com.hicham.wcstoreapp.ui.CurrencyFormatter
import com.hicham.wcstoreapp.ui.cart.CartViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val currencyFormatProvider: CurrencyFormatProvider
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        combine(
            cartRepository.items,
            currencyFormatProvider.formatSettings
        ) { cartItems, formatSettings ->
            Pair(cartItems, CurrencyFormatter(formatSettings))
        }.onEach { (cartItems, currencyFormatter) ->
            val totalPrice = currencyFormatter.format(
                price = cartItems.sumOf { it.product.price * it.quantity.toBigDecimal() }
            )
            _uiState.update { state ->
                state.copy(
                    subtotalFormatted = totalPrice,
                    totalFormatted = totalPrice
                )
            }
        }.launchIn(viewModelScope)
    }

    data class UiState(
        val shippingAddress: Address? = null,
        val isBillingSameAsShippingAddress: Boolean = true,
        val billingAddress: Address? = null,
        val subtotalFormatted: String = "",
        val totalFormatted: String = ""
    ) {
        val shippingCost: String = "Free" // TODO
    }
}