package com.hicham.wcstoreapp.data.cart.db

import com.hicham.wcstoreapp.data.storeApi.NetworkCart
import com.hicham.wcstoreapp.data.storeApi.NetworkCartItem
import com.hicham.wcstoreapp.data.db.CartItemEntity
import com.hicham.wcstoreapp.data.db.daos.AddressDao
import com.hicham.wcstoreapp.data.db.daos.CartDao
import com.hicham.wcstoreapp.models.CartItemTotals
import com.hicham.wcstoreapp.models.CartTotals
import com.hicham.wcstoreapp.util.DB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CartUpdateService(private val cartDao: CartDao, private val addressDao: AddressDao) {
    /**
     * Updates local storage using the received cart.
     * This will make sure to match set the primary addresses ids if they are present in the database
     */
    suspend fun updateCart(networkCart: NetworkCart) = withContext(Dispatchers.DB) {
        val shippingAddressId = addressDao.getMatchingAddress(networkCart.shippingAddress)?.id
        val billingAddressId = addressDao.getMatchingAddress(networkCart.billingAddress)?.id

        cartDao.transaction {
            cartDao.clear()
            val totals = with(networkCart.totals) {
                CartTotals(
                    subtotal = calculatePrice(totalItems),
                    tax = calculatePrice(totalTax),
                    shippingEstimate = totalShipping?.let { calculatePrice(it) },
                    total = calculatePrice(totalPrice)
                )
            }
            cartDao.insert(
                primaryBillingAddress = billingAddressId,
                primaryShippingAddress = shippingAddressId,
                subtotal = totals.subtotal,
                tax = totals.tax,
                shippingEstimate = totals.shippingEstimate,
                total = totals.total
            )
            networkCart.items.forEach {
                cartDao.upsertCartItem(it.toEntity())
            }
        }
    }

    private fun NetworkCartItem.toEntity(): CartItemEntity {
        val totals = with(totals) {
            CartItemTotals(
                subtotal = calculatePrice(lineSubtotal),
                tax = calculatePrice(lineSubtotalTax),
                total = calculatePrice(lineTotal)
            )
        }
        return CartItemEntity(
            key = key,
            productId = id,
            quantity = quantity,
            subtotal = totals.subtotal,
            tax = totals.tax,
            total = totals.total
        )
    }
}
