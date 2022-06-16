package com.hicham.wcstoreapp.data.checkout

import com.hicham.wcstoreapp.models.Address
import com.hicham.wcstoreapp.models.CheckoutData
import com.hicham.wcstoreapp.models.PaymentMethod
import kotlinx.coroutines.flow.Flow

interface CheckoutRepository {
    suspend fun placeOrder(
        shippingAddress: Address,
        billingAddress: Address,
        paymentMethod: PaymentMethod
    ): Result<Long>
}