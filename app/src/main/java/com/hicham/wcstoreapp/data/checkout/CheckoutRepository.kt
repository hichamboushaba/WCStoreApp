package com.hicham.wcstoreapp.data.checkout

import com.hicham.wcstoreapp.models.Address
import com.hicham.wcstoreapp.models.CheckoutData
import com.hicham.wcstoreapp.models.PaymentMethod
import kotlinx.coroutines.flow.Flow

interface CheckoutRepository {
    val checkout: Flow<CheckoutData>
    suspend fun updateShippingAddress(shippingAddress: Address): Result<Unit>
    suspend fun updateBillingAddress(billingAddress: Address): Result<Unit>
    suspend fun updatePaymentMethod(paymentMethod: PaymentMethod): Result<Unit>
    suspend fun processCheckout(): Long
}