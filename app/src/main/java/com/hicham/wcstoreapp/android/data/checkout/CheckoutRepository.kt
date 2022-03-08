package com.hicham.wcstoreapp.android.data.checkout

import com.hicham.wcstoreapp.android.models.Address
import com.hicham.wcstoreapp.android.models.CheckoutData
import com.hicham.wcstoreapp.android.models.PaymentMethod
import kotlinx.coroutines.flow.Flow

interface CheckoutRepository {
    val checkout: Flow<CheckoutData>
    suspend fun updatePaymentMethod(paymentMethod: PaymentMethod): Result<Unit>
    suspend fun placeOrder(shippingAddress: Address, billingAddress: Address): Result<Long>
}