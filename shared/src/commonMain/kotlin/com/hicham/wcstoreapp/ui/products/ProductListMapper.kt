package com.hicham.wcstoreapp.ui.products

import com.hicham.wcstoreapp.android.data.cart.CartRepository
import com.hicham.wcstoreapp.android.data.cart.items
import com.hicham.wcstoreapp.data.currencyformat.CurrencyFormatProvider
import com.hicham.wcstoreapp.data.product.ProductsListState
import com.hicham.wcstoreapp.ui.CurrencyFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

fun Flow<ProductsListState>.mapToUiModel(
    currencyFormatProvider: CurrencyFormatProvider,
    cartRepository: CartRepository
): Flow<ProductsUiListState> {
    return combine(
        this,
        cartRepository.items,
        currencyFormatProvider.formatSettings.map { CurrencyFormatter(it) }
    ) { listState, cartItems, formatter ->
        // logcat { "combine" }
        ProductsUiListState(
            products = listState.products.map { product ->
                ProductUiModel(
                    product = product,
                    priceFormatted = formatter.format(product.prices.price),
                    quantityInCart = cartItems.firstOrNull { it.product == product }?.quantity ?: 0
                )
            },
            hasNext = listState.hasNext,
            state = listState.state
        )
    }
        .flowOn(Dispatchers.Default)
}