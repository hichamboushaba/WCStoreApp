package com.hicham.wcstoreapp.data.source.fake

import com.hicham.wcstoreapp.data.source.OrderRepository
import com.hicham.wcstoreapp.models.Address
import com.hicham.wcstoreapp.models.CartItem
import com.hicham.wcstoreapp.models.PaymentMethod
import kotlinx.coroutines.delay
import javax.inject.Inject

class FakeOrderRepository @Inject constructor() : OrderRepository {
    override suspend fun createOrder(
        items: List<CartItem>,
        shippingAddress: Address,
        billingAddress: Address,
        paymentMethod: PaymentMethod
    ): Result<Long> {
        delay(500)
        return Result.success(3312L)
    }
}