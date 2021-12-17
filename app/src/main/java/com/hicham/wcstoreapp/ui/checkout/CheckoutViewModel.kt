package com.hicham.wcstoreapp.ui.checkout

import androidx.lifecycle.viewModelScope
import com.hicham.wcstoreapp.data.address.AddressRepository
import com.hicham.wcstoreapp.data.cart.CartRepository
import com.hicham.wcstoreapp.data.cart.items
import com.hicham.wcstoreapp.data.checkout.CheckoutRepository
import com.hicham.wcstoreapp.data.currencyformat.CurrencyFormatProvider
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
import logcat.asLog
import logcat.logcat
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val currencyFormatProvider: CurrencyFormatProvider,
    private val addressRepository: AddressRepository,
    private val navigationManager: NavigationManager,
    private val checkoutRepository: CheckoutRepository
) : BaseViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        combine(
            checkoutRepository.checkout,
            cartRepository.items,
            currencyFormatProvider.formatSettings
        ) { checkoutData, cartItems, formatSettings ->
            val currencyFormatter = CurrencyFormatter(formatSettings)
            val totalPrice = currencyFormatter.format(
                price = cartItems.sumOf { it.product.prices.price * it.quantity.toBigDecimal() }
            )
            _uiState.update { state ->
                state.copy(
                    selectedPaymentMethod = checkoutData.paymentMethod,
                    subtotalFormatted = totalPrice,
                    totalFormatted = totalPrice
                )
            }
        }
            .onStart { _uiState.update { it.copy(isLoading = true) } }
            .onEach { _uiState.update { it.copy(isLoading = false) } }
            .catch {
                logcat { it.asLog() }
                TODO("Handle error")
            }
            .launchIn(viewModelScope)

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

    fun onChangePaymentMethodClicked() {
        _uiState.update { it.copy(isShowingPaymentMethodSelector = true) }
    }

    fun onPaymentMethodSelected(paymentMethod: PaymentMethod) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isShowingPaymentMethodSelector = false) }
            if (paymentMethod != _uiState.value.selectedPaymentMethod) {
                checkoutRepository.updatePaymentMethod(paymentMethod).onFailure {
                    //TODO
                }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun onPlacedOrderClicked() {
        viewModelScope.launch {
            _uiState.update { state -> state.copy(isLoading = true) }
            val shipping = uiState.value.shippingAddress!!

            val result = checkoutRepository.placeOrder(
                shippingAddress = shipping,
                billingAddress = uiState.value.billingAddress ?: shipping,
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
        val isShowingPaymentMethodSelector: Boolean = false,
        val shippingAddress: Address? = null,
        val isBillingSameAsShippingAddress: Boolean = true,
        val billingAddress: Address? = null,
        val selectedPaymentMethod: PaymentMethod? = null,
        val subtotalFormatted: String = "",
        val taxFormatted: String = "",
        val totalFormatted: String = ""
    ) {
        val shippingCost: String = "Free" // TODO
    }
}