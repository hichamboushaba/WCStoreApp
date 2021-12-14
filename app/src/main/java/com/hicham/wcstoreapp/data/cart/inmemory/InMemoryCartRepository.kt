package com.hicham.wcstoreapp.data.cart.inmemory

import com.hicham.wcstoreapp.data.cart.CartRepository
import com.hicham.wcstoreapp.models.Cart
import com.hicham.wcstoreapp.models.CartItem
import com.hicham.wcstoreapp.models.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class InMemoryCartRepository @Inject constructor() : CartRepository {
    private val _cart = MutableStateFlow(Cart(emptyList()))
    override val cart: StateFlow<Cart> = _cart.asStateFlow()

    override suspend fun addItem(product: Product): Result<Unit> {
        _cart.update { cart ->
            cart.copy(items = cart.items.toMutableList().apply {
                val index = indexOfFirst { it.product == product }
                if (index == -1) {
                    add(CartItem(id = "", product, 1))
                } else {
                    val currentItem = get(index)
                    set(index, currentItem.copy(quantity = currentItem.quantity + 1))
                }
            })
        }
        return Result.success(Unit)
    }

    override suspend fun deleteItem(product: Product): Result<Unit> {
        _cart.update { cart ->
            cart.copy(items = cart.items
                .map {
                    if (it.product == product) it.copy(quantity = it.quantity - 1) else it
                }
                .filter { it.quantity > 0 }
            )
        }
        return Result.success(Unit)
    }

    override suspend fun clearProduct(product: Product): Result<Unit> {
        _cart.update { cart ->
            cart.copy(items = cart.items.filterNot { it.product == product })
        }
        return Result.success(Unit)
    }

    override suspend fun clear(): Result<Unit> {
        _cart.value = Cart(emptyList())
        return Result.success(Unit)
    }
}