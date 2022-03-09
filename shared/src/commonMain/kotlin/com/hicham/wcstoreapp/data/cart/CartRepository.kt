package com.hicham.wcstoreapp.android.data.cart

import com.hicham.wcstoreapp.models.Cart
import com.hicham.wcstoreapp.models.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface CartRepository {
    val cart: Flow<Cart>
    suspend fun addItem(product: Product): Result<Unit>
    suspend fun deleteItem(product: Product): Result<Unit>
    suspend fun clearProduct(product: Product): Result<Unit>
    suspend fun clear(): Result<Unit>
}

val CartRepository.items
    get() = cart.map { it.items }