package com.hicham.wcstoreapp.ui.checkout

import com.hicham.wcstoreapp.android.data.cart.CartRepository
import com.hicham.wcstoreapp.data.address.AddressRepository
import com.hicham.wcstoreapp.data.checkout.CheckoutRepository
import com.hicham.wcstoreapp.data.currencyformat.CurrencyFormatProvider
import com.hicham.wcstoreapp.models.Address
import com.hicham.wcstoreapp.models.PaymentMethod
import com.hicham.wcstoreapp.ui.*
import com.hicham.wcstoreapp.ui.checkout.address.AddAddressViewModel
import com.hicham.wcstoreapp.ui.navigation.Screen
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CheckoutViewModel constructor(
    private val cartRepository: CartRepository,
    private val currencyFormatProvider: CurrencyFormatProvider,
    private val addressRepository: AddressRepository,
    private val navigationManager: NavigationManager,
    private val checkoutRepository: CheckoutRepository
) : BaseViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    //TODO fetch selected payment method on launch
    private val availablePaymentMethods =
        MutableStateFlow<List<PaymentMethod>>(listOf(PaymentMethod.CASH, PaymentMethod.WIRE))
    private val selectedPaymentMethod = MutableStateFlow<PaymentMethod>(PaymentMethod.CASH)

    private val _retryTrigger = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    init {
        combine(
            selectedPaymentMethod,
            availablePaymentMethods,
            cartRepository.cart,
            currencyFormatProvider.formatSettings
        ) { paymentMethod, availablePaymentMethods, cart, formatSettings ->
            val currencyFormatter = CurrencyFormatter(formatSettings)
            _uiState.update { state ->
                state.copy(
                    isLoading = false,
                    loadingFailed = false,
                    selectedPaymentMethod = paymentMethod,
                    availablePaymentMethods = availablePaymentMethods,
                    subtotalFormatted = currencyFormatter.format(cart.totals.subtotal),
                    taxFormatted = currencyFormatter.format(cart.totals.tax),
                    shippingCost = cart.totals.shippingEstimate?.let { currencyFormatter.format(it) },
                    totalFormatted = currencyFormatter.format(cart.totals.total)
                )
            }
        }
            .onStart { _uiState.update { it.copy(isLoading = true) } }
            .retry {
                _uiState.update { it.copy(loadingFailed = true) }
                _retryTrigger.first()
                true
            }
            .launchIn(viewModelScope)

        observeShippingAddress()
    }

    private fun observeShippingAddress() {
        // Observe Added Address
        navigationManager.observeResultAsFlow<Address>(AddAddressViewModel.ADDRESS_RESULT)
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

    fun onAddPaymentMethod(paymentMethod: PaymentMethod) {
        availablePaymentMethods.update { it + paymentMethod }
        onPaymentMethodSelected(paymentMethod)
    }

    fun onPaymentMethodSelected(paymentMethod: PaymentMethod) {
        selectedPaymentMethod.value = paymentMethod
        _uiState.update { it.copy(isShowingPaymentMethodSelector = false) }
    }

    fun onPlacedOrderClicked() {
        viewModelScope.launch {
            _uiState.update { state -> state.copy(isLoading = true) }

            val result = uiState.value.let { state ->
                checkoutRepository.placeOrder(
                    shippingAddress = state.shippingAddress!!,
                    billingAddress = uiState.value.billingAddress ?: state.shippingAddress,
                    paymentMethod = state.selectedPaymentMethod!!
                )
            }

            _uiState.update { state -> state.copy(isLoading = false) }

            result.fold(
                onSuccess = {
                    navigationManager.navigate(Screen.OrderPlaced.createRoute(it))
                    cartRepository.clear()
                },
                onFailure = {
                    triggerEffect(ShowSnackbar("Error while processing the order"))
                }
            )
        }
    }

    fun onRetryClicked() {
        _retryTrigger.tryEmit(Unit)
    }

    data class UiState(
        val isLoading: Boolean = false,
        val loadingFailed: Boolean = false,
        val isShowingPaymentMethodSelector: Boolean = false,
        val shippingAddress: Address? = null,
        val isBillingSameAsShippingAddress: Boolean = true,
        val billingAddress: Address? = null,
        val shippingCost: String? = null,
        val availablePaymentMethods: List<PaymentMethod> = emptyList(),
        val selectedPaymentMethod: PaymentMethod? = null,
        val subtotalFormatted: String = "",
        val taxFormatted: String = "",
        val totalFormatted: String = ""
    ) {
        val isValid = shippingAddress != null && selectedPaymentMethod != null
    }
}