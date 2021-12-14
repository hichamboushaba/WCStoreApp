package com.hicham.wcstoreapp.ui.products

import androidx.paging.PagingData
import androidx.paging.map
import com.hicham.wcstoreapp.data.cart.CartRepository
import com.hicham.wcstoreapp.models.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import logcat.logcat

fun Flow<PagingData<Product>>.mapToUiModel(
    cartRepository: CartRepository
): Flow<PagingData<ProductUiModel>> {
    return combine(
        this,
        cartRepository.items
    ) { pagingData, cartItems ->
        logcat { "combine" }
        pagingData.map { product ->
            ProductUiModel(
                product = product,
                priceFormatted = product.prices.formattedPrice,
                quantityInCart = cartItems.firstOrNull { it.product == product }?.quantity ?: 0
            )
        }
    }
        .flowOn(Dispatchers.Default)
}