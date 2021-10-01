package com.hicham.wcstoreapp.ui.checkout

import androidx.lifecycle.viewModelScope
import com.hicham.wcstoreapp.data.AddressRepository
import com.hicham.wcstoreapp.data.CartRepository
import com.hicham.wcstoreapp.data.CurrencyFormatProvider
import com.hicham.wcstoreapp.data.OrderRepository
import com.hicham.wcstoreapp.models.Address
import com.hicham.wcstoreapp.models.PaymentMethod
import com.hicham.wcstoreapp.ui.BaseViewModel
import com.hicham.wcstoreapp.ui.CurrencyFormatter
import com.hicham.wcstoreapp.ui.checkout.address.AddAddressViewModel
import com.hicham.wcstoreapp.ui.navigation.NavigationManager
import com.hicham.wcstoreapp.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val currencyFormatProvider: CurrencyFormatProvider,
    private val addressRepository: AddressRepository,
    private val navigationManager: NavigationManager,
    private val orderRepository: OrderRepository
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        combine(
            cartRepository.items,
            currencyFormatProvider.formatSettings
        ) { cartItems, formatSettings ->
            val currencyFormatter = CurrencyFormatter(formatSettings)
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

        observeShippingAddress()
    }

    private fun observeShippingAddress() {
        // Observe Added Address
        navigationManager.observeResult<Address>(AddAddressViewModel.ADDRESS_RESULT)
            .onEach {
                addressRepository.setPrimaryShippingAddress(it)
            }.launchIn(viewModelScope)

        // Observe primary shipping address
        addressRepository.primaryShippingAddress.onEach {
            _uiState.update { state ->
                state.copy(
                    shippingAddress = it
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onEditShippingAddressClicked() {
        viewModelScope.launch {
            if (addressRepository.savedAddresses.first().isEmpty()) {
                navigationManager.navigate(Screen.AddAddress.route)
            } else {
                navigationManager.navigate(Screen.AddressList.route)
            }
        }
    }

    fun onPlacedOrderClicked() {
        viewModelScope.launch {
            _uiState.update { state -> state.copy(isLoading = true) }
            val cartItems = cartRepository.items.first()
            val shipping = uiState.value.shippingAddress!!

            val result = orderRepository.createOrder(
                items = cartItems,
                shippingAddress = shipping,
                billingAddress = uiState.value.billingAddress ?: shipping,
                paymentMethod = uiState.value.selectedPaymentMethod
            )

            _uiState.update { state -> state.copy(isLoading = false) }

            result.fold(
                onSuccess = {
                    cartRepository.clear()
                    navigationManager.navigate(Screen.OrderPlaced.createRoute(it)) {
                        popUpTo(Screen.Home.route)
                    }
                },
                onFailure = {
                    //TODO show a snackbar
                }
            )
        }
    }

    data class UiState(
        val isLoading: Boolean = false,
        val shippingAddress: Address? = null,
        val isBillingSameAsShippingAddress: Boolean = true,
        val billingAddress: Address? = null,
        val selectedPaymentMethod: PaymentMethod = PaymentMethod.CASH,
        val subtotalFormatted: String = "",
        val totalFormatted: String = ""
    ) {
        val shippingCost: String = "Free" // TODO
    }
}