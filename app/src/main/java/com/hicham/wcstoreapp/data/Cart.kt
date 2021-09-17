package com.hicham.wcstoreapp.data

import com.hicham.wcstoreapp.models.Product
import kotlinx.coroutines.flow.StateFlow

interface Cart {
    val items: StateFlow<List<Product>>
    fun addItem(product: Product)
    fun deleteItem(product: Product)
    fun clearProduct(product: Product)
}