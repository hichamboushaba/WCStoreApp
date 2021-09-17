package com.hicham.wcstoreapp.data.source.inmemory

import com.hicham.wcstoreapp.data.Cart
import com.hicham.wcstoreapp.models.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class InMemoryCart @Inject constructor() : Cart {
    private val _items = MutableStateFlow(emptyList<Product>())
    override val items: StateFlow<List<Product>> = _items.asStateFlow()

    override fun addItem(product: Product) {
        _items.update { list -> list + product }
    }

    override fun deleteItem(product: Product) {
        _items.update { list -> list - product }
    }

    override fun clearProduct(product: Product) {
        _items.update { list -> list.filterNot { it == product } }
    }
}