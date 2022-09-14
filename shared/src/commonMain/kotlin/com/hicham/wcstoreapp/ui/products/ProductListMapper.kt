package com.hicham.wcstoreapp.ui.products

import com.hicham.wcstoreapp.android.data.cart.CartRepository
import com.hicham.wcstoreapp.android.data.cart.items
import com.hicham.wcstoreapp.data.currencyformat.CurrencyFormatProvider
import com.hicham.wcstoreapp.data.product.ProductsListState
import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.ui.BaseViewModel
import com.hicham.wcstoreapp.ui.CurrencyFormatter
import com.hicham.wcstoreapp.ui.ShowSnackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

fun Flow<ProductsListState>.mapToUiModel(
    viewModel: BaseViewModel,
    currencyFormatProvider: CurrencyFormatProvider,
    cartRepository: CartRepository
): Flow<ProductsUiListState> {
    return combine(
        this,
        cartRepository.items,
        currencyFormatProvider.formatSettings.map { CurrencyFormatter(it) }
    ) { listState, cartItems, formatter ->
        ProductsUiListState(
            products = listState.products.map { product ->
                ProductUiModel(
                    product = product,
                    priceFormatted = formatter.format(product.prices.price),
                    quantityInCart = cartItems.firstOrNull { it.product == product }?.quantity ?: 0,
                    addToCart = {
                        viewModel.viewModelScope.launch {
                            cartRepository.addItem(product).onFailure {
                                viewModel.triggerEffect(ShowSnackbar("Error while updating your cart"))
                            }
                        }
                    },
                    deleteFromCart = {
                        viewModel.viewModelScope.launch {
                            cartRepository.deleteItem(product).onFailure {
                                viewModel.triggerEffect(ShowSnackbar("Error while updating your cart"))
                            }
                        }
                    }
                )
            },
            hasNext = listState.hasNext,
            state = listState.state
        )
    }
        .flowOn(Dispatchers.Default)
}