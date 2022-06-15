package com.hicham.wcstoreapp.data.db.daos

import com.hicham.wcstoreapp.Database
import com.hicham.wcstoreapp.data.db.CartEntity
import com.hicham.wcstoreapp.data.db.CartItemEntity
import com.hicham.wcstoreapp.data.db.CartItemWithProduct
import com.hicham.wcstoreapp.data.db.CartWithItemsEntity
import com.hicham.wcstoreapp.util.DB
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.squareup.sqldelight.Transacter
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

class CartDao(private val database: Database) : Transacter by database {
    private val cartQueries = database.cartEntityQueries
    private val cartItemQueries = database.cartItemEntityQueries
    private val productQueries = database.productEntityQueries

    fun observeCart(): Flow<CartWithItemsEntity?> = transactionWithResult {
        combine(
            cartQueries.selectAll()
                .asFlow()
                .mapToOneOrNull(), getCartItemsWithProducts()
        ) { cartEntity, items ->
            if (cartEntity == null) {
                null
            } else {
                CartWithItemsEntity(
                    cartEntity = cartEntity,
                    items = items
                )
            }
        }
    }

    suspend fun getCart(): CartEntity? = withContext(Dispatchers.Default) {
        cartQueries.selectAll().executeAsOneOrNull()
    }

    suspend fun getCartItems(): List<CartItemEntity> = withContext(Dispatchers.Default) {
        cartItemQueries.selectAll().executeAsList()
    }

    suspend fun clear() = withContext(Dispatchers.DB) {
        transaction {
            cartItemQueries.deleteAll()
            cartQueries.deleteAll()
        }
    }

    suspend fun insert(
        primaryBillingAddress: Long?,
        primaryShippingAddress: Long?,
        subtotal: BigDecimal,
        tax: BigDecimal,
        shippingEstimate: BigDecimal?,
        total: BigDecimal
    ) = withContext(Dispatchers.DB) {
        cartQueries.insert(
            primaryBillingAddress = primaryBillingAddress,
            primaryShippingAddress = primaryShippingAddress,
            subtotal = subtotal,
            tax = tax,
            shippingEstimate = shippingEstimate,
            total = total
        )
    }

    suspend fun upsertCartItem(vararg cartItemEntity: CartItemEntity) =
        withContext(Dispatchers.DB) {
            transaction {
                cartItemEntity.forEach {
                    cartItemQueries.upsert(it)
                }
            }
        }

    suspend fun getCartItem(productId: Long): CartItemEntity? = withContext(Dispatchers.Default) {
        cartItemQueries.getCartItemWithProductId(productId).executeAsOneOrNull()
    }

    suspend fun deleteCartItemForProductId(productId: Long) = withContext(Dispatchers.DB) {
        cartItemQueries.deleteCartItemForProductId(productId)
    }

    private fun getCartItemsWithProducts() = cartItemQueries.selectAll().asFlow().mapToList()
        .map { list ->
            list.map {
                CartItemWithProduct(
                    cartItem = it,
                    product = productQueries.selectProduct(it.productId).executeAsOneOrNull()
                )
            }
        }
        .flowOn(Dispatchers.Default)
}