package com.hicham.wcstoreapp.android.data.cart.db

import androidx.room.withTransaction
import com.hicham.wcstoreapp.android.data.db.AppDatabase
import com.hicham.wcstoreapp.android.data.db.entities.CartEntity
import com.hicham.wcstoreapp.android.data.db.entities.toEntity
import com.hicham.wcstoreapp.data.api.NetworkCart
import com.hicham.wcstoreapp.models.CartTotals

class CartUpdateService(private val database: AppDatabase) {
    private val addressDao = database.addressDao()
    private val cartDao = database.cartDao()

    /**
     * Updates local storage using the received cart.
     * This will make sure to match set the primary addresses ids if they are present in the database
     */
    suspend fun updateCart(networkCart: NetworkCart) {
        database.withTransaction {
            database.withTransaction {
                cartDao.clear()
                val shippingAddressId =
                    addressDao.getMatchingAddress(networkCart.shippingAddress)?.id

                val billingAddressId = addressDao.getMatchingAddress(networkCart.billingAddress)?.id
                val totals = with(networkCart.totals) {
                    CartTotals(
                        subtotal = calculatePrice(totalItems),
                        tax = calculatePrice(totalTax),
                        shippingEstimate = totalShipping?.let { calculatePrice(it) },
                        total = calculatePrice(totalPrice)
                    )
                }
                val updatedCart = CartEntity(
                    primaryBillingAddress = billingAddressId,
                    primaryShippingAddress = shippingAddressId,
                    totals = totals
                )
                cartDao.insertCart(updatedCart)
                cartDao.insertItem(*networkCart.items.map {
                    it.toEntity()
                }.toTypedArray())
            }
        }
    }
}