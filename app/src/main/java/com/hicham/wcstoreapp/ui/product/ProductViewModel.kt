package com.hicham.wcstoreapp.ui.product

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hicham.wcstoreapp.data.CartRepository
import com.hicham.wcstoreapp.data.CurrencyFormatProvider
import com.hicham.wcstoreapp.data.ProductsRepository
import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.ui.CurrencyFormatter
import com.hicham.wcstoreapp.ui.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val productsRepository: ProductsRepository,
    private val currencyFormatProvider: CurrencyFormatProvider,
    private val cartRepository: CartRepository
) : ViewModel() {
    private val productId = savedStateHandle.get<Long>(Screen.Product.productIdKey)!!

    private val _uiState = MutableStateFlow<UiState>(UiState.LoadingState)
    val uiState = _uiState.asStateFlow()

    init {
        val productFlow = flow { emit(productsRepository.getProduct(productId)) }
        val cartQuantityFlow = cartRepository.items.map { it.filter { it.id == productId }.size }
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
                    priceFormatted = currencyFormatter.format(product.price),
                    quantityInCart = cartQuantity
                )
            }
            .catch {
                _uiState.value = UiState.ErrorState
            }
            .launchIn(viewModelScope)
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