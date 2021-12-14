package com.hicham.wcstoreapp.data.cart

import com.hicham.wcstoreapp.models.CartItem
import com.hicham.wcstoreapp.models.Product
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    val items: Flow<List<CartItem>>
    suspend fun addItem(product: Product): Result<Unit>
    suspend fun deleteItem(product: Product): Result<Unit>
    suspend fun clearProduct(product: Product): Result<Unit>
    suspend fun clear(): Result<Unit>
}