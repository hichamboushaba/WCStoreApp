package com.hicham.wcstoreapp.data.order

import com.hicham.wcstoreapp.models.Address
import com.hicham.wcstoreapp.models.CartItem
import com.hicham.wcstoreapp.models.PaymentMethod

interface OrderRepository {
    suspend fun createOrder(
        items: List<CartItem>,
        shippingAddress: Address,
        billingAddress: Address,
        paymentMethod: PaymentMethod
    ): Result<Long>
}