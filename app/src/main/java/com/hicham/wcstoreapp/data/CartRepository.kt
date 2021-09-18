package com.hicham.wcstoreapp.data

import com.hicham.wcstoreapp.models.Product
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    val items: Flow<List<Product>>
    fun addItem(product: Product)
    fun deleteItem(product: Product)
    fun clearProduct(product: Product)
}