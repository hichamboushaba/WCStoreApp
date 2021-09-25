package com.hicham.wcstoreapp.data.source.db

import com.hicham.wcstoreapp.data.CartRepository
import com.hicham.wcstoreapp.data.source.db.entities.CartItemEntity
import com.hicham.wcstoreapp.di.AppCoroutineScope
import com.hicham.wcstoreapp.models.CartItem
import com.hicham.wcstoreapp.models.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DBCartRepository @Inject constructor(
    database: AppDatabase,
    @AppCoroutineScope appCoroutineScope: CoroutineScope
) : CartRepository {
    private val cartDao = database.cartDao()

    override val items: Flow<List<CartItem>> = cartDao.getCartItems().map { list ->
        list.mapNotNull { cartItem ->
            // Can't happen due to the foreign key we have now
            // TODO check what's the best way to handle this
            if (cartItem.product != null) {
                CartItem(
                    cartItem.product.toProduct(),
                    quantity = cartItem.cartItem.quantity
                )
            } else null
        }
    }
        .distinctUntilChanged()
        .shareIn(appCoroutineScope, started = SharingStarted.Lazily, replay = 1)

    override suspend fun addItem(product: Product) {
        val currentItem = cartDao.getCartItem(product.id)
        if (currentItem != null) {
            cartDao.updateItem(currentItem.copy(quantity = currentItem.quantity + 1))
        } else {
            cartDao.insertItem(CartItemEntity(productId = product.id, quantity = 1))
        }
    }

    override suspend fun deleteItem(product: Product) {
        val currentItem = cartDao.getCartItem(product.id)
        if (currentItem != null && currentItem.quantity > 1) {
            cartDao.updateItem(currentItem.copy(quantity = currentItem.quantity - 1))
        } else {
            cartDao.deleteCartItemForProductId(productId = product.id)
        }
    }

    override suspend fun clearProduct(product: Product) {
        cartDao.deleteCartItemForProductId(productId = product.id)
    }
}