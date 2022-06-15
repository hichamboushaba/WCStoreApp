package com.hicham.wcstoreapp.data.db.daos

import com.hicham.wcstoreapp.Database
import com.hicham.wcstoreapp.data.db.*
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.squareup.sqldelight.Transacter
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext

class CartDao(private val database: Database) : Transacter by database {
    private val cartQueries = database.cartEntityQueries
    private val cartItemQueries = database.cartItemEntityQueries

    fun observeCart(): Flow<CartWithItemsEntity?> = combine(
        cartQueries.selectAll()
            .asFlow()
            .mapToOneOrNull(),
        getCartItemsWithProducts()
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

    suspend fun getCart(): CartEntity? = withContext(Dispatchers.Default) {
        cartQueries.selectAll().executeAsOneOrNull()
    }

    suspend fun getCartItems(): List<CartItemEntity> = withContext(Dispatchers.Default) {
        cartItemQueries.selectAll().executeAsList()
    }

    fun clear() = transaction {
        cartItemQueries.deleteAll()
        cartQueries.deleteAll()
    }

    fun insert(
        primaryBillingAddress: Long?,
        primaryShippingAddress: Long?,
        subtotal: BigDecimal,
        tax: BigDecimal,
        shippingEstimate: BigDecimal?,
        total: BigDecimal
    ) {
        cartQueries.insert(
            primaryBillingAddress = primaryBillingAddress,
            primaryShippingAddress = primaryShippingAddress,
            subtotal = subtotal,
            tax = tax,
            shippingEstimate = shippingEstimate,
            total = total
        )
    }

    fun upsertCartItem(vararg cartItemEntity: CartItemEntity) = transaction {
        cartItemEntity.forEach {
            cartItemQueries.upsert(it)
        }
    }

    suspend fun getCartItem(productId: Long): CartItemEntity? = withContext(Dispatchers.Default) {
        cartItemQueries.getCartItemWithProductId(productId).executeAsOneOrNull()
    }

    fun deleteCartItemForProductId(productId: Long) {
        cartItemQueries.deleteCartItemForProductId(productId)
    }

    private fun getCartItemsWithProducts() =
        cartItemQueries
            .selectItemsWithProducts { key, productId, quantity, subtotal, tax, total, id, name, images, price, regularPrice, salePrice, shortDescription, description ->
                CartItemWithProduct(
                    cartItem = CartItemEntity(
                        key = key,
                        productId = productId,
                        quantity = quantity,
                        subtotal = subtotal,
                        tax = tax,
                        total = total
                    ),
                    product = ProductEntity(
                        id = id,
                        name = name,
                        images = images,
                        price = price,
                        regularPrice = regularPrice,
                        salePrice = salePrice,
                        shortDescription = shortDescription,
                        description = description
                    )
                )
            }
            .asFlow()
            .mapToList()
}