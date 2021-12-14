package com.hicham.wcstoreapp.data.cart.db

import androidx.room.withTransaction
import com.hicham.wcstoreapp.data.api.NetworkCart
import com.hicham.wcstoreapp.data.api.WooCommerceApi
import com.hicham.wcstoreapp.data.cart.CartRepository
import com.hicham.wcstoreapp.data.db.AppDatabase
import com.hicham.wcstoreapp.data.db.entities.toEntity
import com.hicham.wcstoreapp.di.AppCoroutineScope
import com.hicham.wcstoreapp.models.CartItem
import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.models.toDomainModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import logcat.LogPriority
import logcat.asLog
import logcat.logcat
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DBCartRepository @Inject constructor(
    private val database: AppDatabase,
    @AppCoroutineScope appCoroutineScope: CoroutineScope,
    private val wooCommerceApi: WooCommerceApi
) : CartRepository {
    private val cartDao = database.cartDao()

    override val items: Flow<List<CartItem>> = cartDao.getCartItemsWithProducts()
        .map { list ->
            list.mapNotNull { cartItem ->
                // Can't happen due to the foreign key we have now
                // TODO check what's the best way to handle this
                if (cartItem.product != null) {
                    CartItem(
                        id = cartItem.cartItem.key,
                        product = cartItem.product.toDomainModel(),
                        quantity = cartItem.cartItem.quantity
                    )
                } else null
            }
        }
        .onStart {
            appCoroutineScope.launch {
                fetchCart()
            }
        }
        .distinctUntilChanged()
        .shareIn(appCoroutineScope, started = SharingStarted.WhileSubscribed(60000), replay = 1)

    override suspend fun fetchCart() {
        val networkCart = wooCommerceApi.getCart()
        saveCart(networkCart)
    }

    override suspend fun addItem(product: Product) {
        val currentItem = cartDao.getCartItem(product.id)
        if (currentItem != null) {
            // Optimistic update
            cartDao.updateItem(currentItem.copy(quantity = currentItem.quantity + 1))
        }

        val networkCart = wooCommerceApi.addItemToCart(product.id)
        saveCart(networkCart)
    }

    override suspend fun deleteItem(product: Product) {
        val currentItem = cartDao.getCartItem(product.id)
        if (currentItem != null && currentItem.quantity > 1) {
            cartDao.updateItem(currentItem.copy(quantity = currentItem.quantity - 1))
        } else {
            cartDao.deleteCartItemForProductId(productId = product.id)
        }

        val networkCart = wooCommerceApi.addItemToCart(product.id, quantity = -1)
        saveCart(networkCart)
    }

    override suspend fun clearProduct(product: Product) {
        cartDao.deleteCartItemForProductId(productId = product.id)
    }

    override suspend fun clear() {
        val cartContent = cartDao.getCartItems()
        cartDao.clear()
        try {
            wooCommerceApi.clearCart()
        } catch (e: Exception) {
            logcat(priority = LogPriority.WARN) {
                e.asLog()
            }
            cartDao.insertItem(*cartContent.toTypedArray())
            // TODO return failure here
        }
    }

    private suspend fun saveCart(networkCart: NetworkCart) {
        database.withTransaction {
            cartDao.clear()
            cartDao.insertItem(*networkCart.items.map {
                it.toEntity()
            }.toTypedArray())
        }
    }
}