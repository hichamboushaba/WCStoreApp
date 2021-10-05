package com.hicham.wcstoreapp.data.cart.inmemory

import com.hicham.wcstoreapp.data.cart.CartRepository
import com.hicham.wcstoreapp.models.CartItem
import com.hicham.wcstoreapp.models.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class InMemoryCartRepository @Inject constructor() : CartRepository {
    private val _items = MutableStateFlow(emptyList<CartItem>())
    override val items: StateFlow<List<CartItem>> = _items.asStateFlow()

    override suspend fun addItem(product: Product) {
        _items.update { list ->
            list.toMutableList().apply {
                val index = indexOfFirst { it.product == product }
                if (index == -1) {
                    add(CartItem(product, 1))
                } else {
                    val currentItem = get(index)
                    set(index, currentItem.copy(quantity = currentItem.quantity + 1))
                }
            }
        }
    }

    override suspend fun deleteItem(product: Product) {
        _items.update { list ->
            list
                .map {
                    if (it.product == product) it.copy(quantity = it.quantity - 1) else it
                }
                .filter { it.quantity > 0 }
        }
    }

    override suspend fun clearProduct(product: Product) {
        _items.update { list -> list.filterNot { it.product == product } }
    }

    override suspend fun clear() {
        _items.value = emptyList()
    }
}