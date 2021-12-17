package com.hicham.wcstoreapp.data.cart.inmemory

import com.hicham.wcstoreapp.data.cart.CartRepository
import com.hicham.wcstoreapp.models.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.math.BigDecimal
import javax.inject.Inject

class InMemoryCartRepository @Inject constructor() : CartRepository {
    private val _items = MutableStateFlow(emptyList<CartItem>())
    override val cart: Flow<Cart> = _items.map {
        val totalPrice = it.sumOf { it.product.prices.price }
        Cart(
            items = it,
            totals = CartTotals(
                subtotal = totalPrice,
                tax = BigDecimal.ZERO,
                shippingEstimate = null,
                total = totalPrice
            )
        )
    }

    override suspend fun addItem(product: Product): Result<Unit> {
        _items.update { list ->
            list.toMutableList().apply {
                val index = indexOfFirst { it.product == product }
                if (index == -1) {
                    add(CartItem(id = "", product, 1, product.calculateItemTotals(1)))
                } else {
                    val currentItem = get(index)
                    val newQuantity = currentItem.quantity + 1
                    set(
                        index,
                        currentItem.copy(
                            quantity = newQuantity,
                            totals = product.calculateItemTotals(newQuantity)
                        )
                    )
                }
            }
        }
        return Result.success(Unit)
    }

    override suspend fun deleteItem(product: Product): Result<Unit> {
        _items.update { list ->
            list
                .map {
                    if (it.product == product) {
                        val newQuantity = it.quantity - 1
                        it.copy(
                            quantity = it.quantity - 1,
                            totals = product.calculateItemTotals(newQuantity)
                        )
                    } else it
                }
                .filter { it.quantity > 0 }
        }
        return Result.success(Unit)
    }

    override suspend fun clearProduct(product: Product): Result<Unit> {
        _items.update { list -> list.filterNot { it.product == product } }
        return Result.success(Unit)
    }

    override suspend fun clear(): Result<Unit> {
        _items.value = emptyList()
        return Result.success(Unit)
    }

    private fun Product.calculateItemTotals(quantity: Int) = CartItemTotals(
        subtotal = prices.price.multiply(quantity.toBigDecimal()),
        tax = BigDecimal.ZERO,
        total = prices.price.multiply(quantity.toBigDecimal())
    )
}