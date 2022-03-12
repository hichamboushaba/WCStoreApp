package com.hicham.wcstoreapp.android.data.cart.db

import androidx.room.withTransaction
import com.hicham.wcstoreapp.android.data.cart.CartRepository
import com.hicham.wcstoreapp.android.data.db.AppDatabase
import com.hicham.wcstoreapp.android.data.db.entities.CartItemEntity
import com.hicham.wcstoreapp.android.data.db.entities.toDomainModel
import com.hicham.wcstoreapp.android.di.HiltAppCoroutineScope
import com.hicham.wcstoreapp.data.api.NetworkCart
import com.hicham.wcstoreapp.data.api.WooCommerceApi
import com.hicham.wcstoreapp.models.*
import com.hicham.wcstoreapp.util.runCatchingNetworkErrors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DBCartRepository  constructor(
    private val database: AppDatabase,
    @HiltAppCoroutineScope private val appCoroutineScope: CoroutineScope,
    private val wooCommerceApi: WooCommerceApi,
    private val cartUpdateService: CartUpdateService
) : CartRepository {
    private val cartDao = database.cartDao()

    private var isExecutingOperation: Boolean = false

    override val cart: Flow<Cart> = cartDao.observeCart()
        .map { entity ->
            Cart(
                totals = entity?.cartEntity?.totals ?: CartTotals.ZERO,
                items = entity?.items.orEmpty().mapNotNull { item ->
                    // Can't happen due to the foreign key we have now
                    // TODO check what's the best way to handle this
                    if (item.product != null) {
                        CartItem(
                            id = item.cartItem.key,
                            product = item.product.toDomainModel(),
                            quantity = item.cartItem.quantity,
                            totals = item.cartItem.totals
                        )
                    } else null
                })
        }
        .onStart {
            fetchCart()
        }
        .distinctUntilChanged()
        .shareIn(appCoroutineScope, started = SharingStarted.WhileSubscribed(60000), replay = 1)

    private fun fetchCart() = appCoroutineScope.launch {
        runCatchingNetworkErrors {
            val networkCart = wooCommerceApi.getCart()
            cartUpdateService.updateCart(networkCart)
        }
    }

    override suspend fun addItem(product: Product): Result<Unit> {
        if (isExecutingOperation) return Result.success(Unit)
        addItemToDb(product)

        return runActionWithOptimisticUpdate(
            action = { wooCommerceApi.addItemToCart(product.id) },
            revertAction = { deleteItemFromDb(product) }
        )
    }

    override suspend fun deleteItem(product: Product): Result<Unit> {
        if (isExecutingOperation) return Result.success(Unit)
        val currentItem = deleteItemFromDb(product)

        return runActionWithOptimisticUpdate(
            action = { wooCommerceApi.addItemToCart(product.id, quantity = -1) },
            revertAction = { addItemToDb(product, cartItemKey = currentItem?.key) }
        )
    }

    override suspend fun clearProduct(product: Product): Result<Unit> {
        if (isExecutingOperation) return Result.success(Unit)
        val currentItem =
            cartDao.getCartItem(product.id) ?: return Result.failure(NullPointerException())
        cartDao.deleteCartItemForProductId(productId = product.id)

        return runActionWithOptimisticUpdate(
            action = { wooCommerceApi.removeItemFromCart(currentItem.key) },
            revertAction = { cartDao.insertItem(currentItem) }
        )
    }

    override suspend fun clear(): Result<Unit> {
        if (isExecutingOperation) return Result.success(Unit)
        val cartContent = cartDao.getCartItems()
        cartDao.clear()

        return runActionWithOptimisticUpdate(
            action = {
                wooCommerceApi.clearCart()
                wooCommerceApi.getCart()
            },
            revertAction = {
                cartDao.insertItem(*cartContent.toTypedArray())
            }
        )
    }

    private suspend fun deleteItemFromDb(product: Product): CartItemEntity? {
        return database.withTransaction {
            val currentItem = cartDao.getCartItem(product.id)
            if (currentItem != null && currentItem.quantity > 1) {
                cartDao.updateItem(currentItem.copy(quantity = currentItem.quantity - 1))
            } else {
                cartDao.deleteCartItemForProductId(productId = product.id)
            }
            currentItem
        }
    }

    private suspend fun addItemToDb(product: Product, cartItemKey: String? = null) {
        database.withTransaction {
            val currentItem = cartDao.getCartItem(product.id)
            if (currentItem != null) {
                cartDao.updateItem(currentItem.copy(quantity = currentItem.quantity + 1))
            } else if (cartItemKey != null) {
                cartDao.insertItem(CartItemEntity(cartItemKey, 1, product.id, CartItemTotals.ZERO))
            }
        }
    }

    private suspend fun runActionWithOptimisticUpdate(
        action: suspend () -> NetworkCart,
        revertAction: suspend () -> Unit
    ): Result<Unit> {
        isExecutingOperation = true
        try {
            return runCatchingNetworkErrors(action)
                .onSuccess {
                    cartUpdateService.updateCart(it)
                }
                .onFailure {
                    // Revert changes
                    revertAction()
                    // Attempt to refresh cart to fix any inconsistencies
                    fetchCart()
                }
                .map { }
        } finally {
            isExecutingOperation = false
        }
    }
}