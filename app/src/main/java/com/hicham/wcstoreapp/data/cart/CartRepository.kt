package com.hicham.wcstoreapp.data.cart

import com.hicham.wcstoreapp.models.CartItem
import com.hicham.wcstoreapp.models.Product
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    val items: Flow<List<CartItem>>
    suspend fun fetchCart()
    suspend fun addItem(product: Product)
    suspend fun deleteItem(product: Product)
    suspend fun clearProduct(product: Product)
    suspend fun clear()
}