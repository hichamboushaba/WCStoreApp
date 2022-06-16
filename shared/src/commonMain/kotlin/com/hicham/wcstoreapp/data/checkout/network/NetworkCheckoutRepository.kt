package com.hicham.wcstoreapp.data.checkout.network

import com.hicham.wcstoreapp.data.api.*
import com.hicham.wcstoreapp.data.checkout.CheckoutRepository
import com.hicham.wcstoreapp.data.payment.PaymentHandler
import com.hicham.wcstoreapp.models.Address
import com.hicham.wcstoreapp.models.CheckoutData
import com.hicham.wcstoreapp.models.PaymentMethod
import com.hicham.wcstoreapp.util.runCatchingNetworkErrors
import kotlinx.coroutines.flow.*

class NetworkCheckoutRepository constructor(
    private val wooCommerceApi: WooCommerceApi,
    private val paymentHandler: PaymentHandler
) : CheckoutRepository {
    override suspend fun placeOrder(
        shippingAddress: Address,
        billingAddress: Address,
        paymentMethod: PaymentMethod
    ): Result<Long> {
        return runCatchingNetworkErrors {
            val paymentData = paymentHandler.processPayment(paymentMethod, billingAddress)
            wooCommerceApi.placeOrder(
                NetworkPlaceOrderRequest(
                    shippingAddress = shippingAddress.toNetworkAddress(),
                    billingAddress = billingAddress.toNetworkAddress()
                        .copy(email = "email@test.com"), // TODO remove this once the email is handled
                    paymentMethod = paymentMethod.value,
                    paymentData = paymentData
                )
            ).orderID
        }
    }
}