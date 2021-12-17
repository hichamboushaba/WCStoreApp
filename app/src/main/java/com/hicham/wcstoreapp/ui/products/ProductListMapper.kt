package com.hicham.wcstoreapp.ui.products

import androidx.paging.PagingData
import androidx.paging.map
import com.hicham.wcstoreapp.data.cart.CartRepository
import com.hicham.wcstoreapp.data.cart.items
import com.hicham.wcstoreapp.data.currencyformat.CurrencyFormatProvider
import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.ui.CurrencyFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import logcat.logcat

fun Flow<PagingData<Product>>.mapToUiModel(
    currencyFormatProvider: CurrencyFormatProvider,
    cartRepository: CartRepository
): Flow<PagingData<ProductUiModel>> {
    return combine(
        this,
        cartRepository.items,
        currencyFormatProvider.formatSettings.map { CurrencyFormatter(it) }
    ) { pagingData, cartItems, formatter ->
        logcat { "combine" }
        pagingData.map { product ->
            ProductUiModel(
                product = product,
                priceFormatted = formatter.format(product.prices.price),
                quantityInCart = cartItems.firstOrNull { it.product == product }?.quantity ?: 0
            )
        }
    }
        .flowOn(Dispatchers.Default)
}