package com.hicham.wcstoreapp.data.cart.db

import com.hicham.wcstoreapp.android.data.cart.CartRepository
import com.hicham.wcstoreapp.data.api.NetworkCart
import com.hicham.wcstoreapp.data.api.WooCommerceApi
import com.hicham.wcstoreapp.data.db.CartItemEntity
import com.hicham.wcstoreapp.data.db.daos.CartDao
import com.hicham.wcstoreapp.data.db.suspendTransaction
import com.hicham.wcstoreapp.data.db.suspendTransactionWithResult
import com.hicham.wcstoreapp.data.db.toDomainModel
import com.hicham.wcstoreapp.models.*
import com.hicham.wcstoreapp.util.runCatchingNetworkErrors
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DBCartRepository constructor(
    private val cartDao: CartDao,
    private val appCoroutineScope: CoroutineScope,
    private val wooCommerceApi: WooCommerceApi,
    private val cartUpdateService: CartUpdateService
) : CartRepository {
    private var isExecutingOperation: Boolean = false

    override val cart: Flow<Cart> = cartDao.observeCart()
        .map { entity ->
            entity?.toDomainModel() ?: Cart(
                totals = CartTotals.ZERO,
                items = emptyList()
            )
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
        val currentItem = cartDao.getCartItem(product.id)
            ?: return Result.failure(NullPointerException())
        cartDao.deleteCartItemForProductId(productId = product.id)

        return runActionWithOptimisticUpdate(
            action = { wooCommerceApi.removeItemFromCart(currentItem.key) },
            revertAction = { cartDao.upsertCartItem(currentItem) }
        )
    }

    override suspend fun clear(): Result<Unit> {
        if (isExecutingOperation) return Result.success(Unit)
        val cart = cartDao.getCart()
        val cartItems = cartDao.getCartItems()
        cartDao.clear()

        return runActionWithOptimisticUpdate(
            action = {
                wooCommerceApi.clearCart()
                wooCommerceApi.getCart()
            },
            revertAction = {
                if (cart != null) {
                    cartDao.insert(
                        primaryBillingAddress = cart.primaryBillingAddress,
                        total = cart.total,
                        tax = cart.tax,
                        subtotal = cart.subtotal,
                        shippingEstimate = cart.shippingEstimate,
                        primaryShippingAddress = cart.primaryShippingAddress
                    )
                }
                cartDao.upsertCartItem(*cartItems.toTypedArray())
            }
        )
    }

    private suspend fun deleteItemFromDb(product: Product): CartItemEntity? {
        return cartDao.suspendTransactionWithResult {
            val currentItem = cartDao.getCartItem(product.id)
            if (currentItem != null && currentItem.quantity > 1) {
                cartDao.upsertCartItem(currentItem.copy(quantity = currentItem.quantity - 1))
            } else {
                cartDao.deleteCartItemForProductId(productId = product.id)
            }
            currentItem
        }
    }

    private suspend fun addItemToDb(product: Product, cartItemKey: String? = null) {
        cartDao.suspendTransaction {
            val currentItem = cartDao.getCartItem(product.id)
            val updatedItem = currentItem?.copy(quantity = currentItem.quantity + 1)
                ?: cartItemKey?.let {
                    CartItemEntity(
                        cartItemKey,
                        product.id,
                        1,
                        subtotal = BigDecimal.ZERO,
                        tax = BigDecimal.ZERO,
                        total = BigDecimal.ZERO
                    )
                }

            updatedItem?.let {
                cartDao.upsertCartItem(updatedItem)
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