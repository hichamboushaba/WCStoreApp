package com.hicham.wcstoreapp.android.ui.product

import androidx.lifecycle.SavedStateHandle
import com.hicham.wcstoreapp.android.data.cart.CartRepository
import com.hicham.wcstoreapp.android.data.cart.items
import com.hicham.wcstoreapp.android.ui.navigation.AndroidNavigationManager
import com.hicham.wcstoreapp.data.currencyformat.CurrencyFormatProvider
import com.hicham.wcstoreapp.data.product.ProductsRepository
import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.ui.BaseViewModel
import com.hicham.wcstoreapp.ui.CurrencyFormatter
import com.hicham.wcstoreapp.ui.ShowActionSnackbar
import com.hicham.wcstoreapp.ui.ShowSnackbar
import com.hicham.wcstoreapp.ui.navigation.Screen
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProductViewModel(
    savedStateHandle: SavedStateHandle,
    private val productsRepository: ProductsRepository,
    private val cartRepository: CartRepository,
    private val navigationManager: AndroidNavigationManager,
    currencyFormatProvider: CurrencyFormatProvider
) : BaseViewModel() {
    private val productId = savedStateHandle.get<Long>(Screen.Product.navArguments.first().name)!!

    private val _uiState = MutableStateFlow<UiState>(UiState.LoadingState)
    val uiState = _uiState.asStateFlow()

    init {
        val productFlow = flow { emit(productsRepository.getProduct(productId)) }
        val cartQuantityFlow = cartRepository.items.map { list ->
            list.firstOrNull {
                it.product.id == productId
            }?.quantity ?: 0
        }
        combine(
            productFlow,
            cartQuantityFlow,
            currencyFormatProvider.formatSettings.map { CurrencyFormatter(it) }
        ) { product, cartQuantity, formatter ->
            _uiState.value = UiState.SuccessState(
                product = product,
                priceFormatted = formatter.format(product.prices.price),
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
            cartRepository.addItem(state.product).fold(
                onSuccess = {
                    triggerEffect(
                        ShowActionSnackbar(
                            "Product added to cart",
                            actionText = "View Cart",
                            action = ::onViewCartClicked
                        )
                    )
                },
                onFailure = {
                    triggerEffect(ShowSnackbar("Error while adding the product to cart"))
                }
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