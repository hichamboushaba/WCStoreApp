package com.hicham.wcstoreapp.ui.product

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.hicham.wcstoreapp.data.cart.CartRepository
import com.hicham.wcstoreapp.data.currencyformat.CurrencyFormatProvider
import com.hicham.wcstoreapp.data.product.ProductsRepository
import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.ui.BaseViewModel
import com.hicham.wcstoreapp.ui.CurrencyFormatter
import com.hicham.wcstoreapp.ui.ShowActionSnackbar
import com.hicham.wcstoreapp.ui.navigation.NavigationManager
import com.hicham.wcstoreapp.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val productsRepository: ProductsRepository,
    private val currencyFormatProvider: CurrencyFormatProvider,
    private val cartRepository: CartRepository,
    private val navigationManager: NavigationManager
) : BaseViewModel() {
    private val productId = savedStateHandle.get<Long>(Screen.Product.navArguments.first().name)!!

    private val _uiState = MutableStateFlow<UiState>(UiState.LoadingState)
    val uiState = _uiState.asStateFlow()

    init {
        val productFlow = flow { emit(productsRepository.getProduct(productId)) }
        val cartQuantityFlow = cartRepository.items.map {
            it.firstOrNull { it.product.id == productId }?.quantity ?: 0
        }
        combine(
            productFlow,
            cartQuantityFlow,
            currencyFormatProvider.formatSettings
        ) { product, cartQuantity, formatSettings ->
            Triple(product, cartQuantity, CurrencyFormatter(formatSettings))
        }
            .onEach { (product, cartQuantity, currencyFormatter) ->
                _uiState.value = UiState.SuccessState(
                    product = product,
                    priceFormatted = currencyFormatter.format(product.prices.price),
                    quantityInCart = cartQuantity
                )
            }
            .catch {
                _uiState.value = UiState.ErrorState
            }
            .launchIn(viewModelScope)
    }

    fun onAddToCartClicked() {
        viewModelScope.launch {
            val state = uiState.value
            if (state !is UiState.SuccessState) return@launch
            cartRepository.addItem(state.product)
            triggerEffect(
                ShowActionSnackbar(
                    "Product added to cart",
                    actionText = "View Cart",
                    action = ::onViewCartClicked
                )
            )
        }
    }

    private fun onViewCartClicked() {
        navigationManager.navigate(Screen.Cart.route)
    }

    fun onBackClicked() {
        navigationManager.navigateUp()
    }

    sealed class UiState {
        object LoadingState : UiState()
        data class SuccessState(
            val product: Product,
            val priceFormatted: String,
            val quantityInCart: Int
        ) : UiState()

        object ErrorState : UiState()
    }
}