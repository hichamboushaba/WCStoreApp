package com.hicham.wcstoreapp.android.data.checkout.network

import com.hicham.wcstoreapp.android.data.api.*
import com.hicham.wcstoreapp.android.data.checkout.CheckoutRepository
import com.hicham.wcstoreapp.android.util.runCatchingNetworkErrors
import com.hicham.wcstoreapp.models.Address
import com.hicham.wcstoreapp.models.CheckoutData
import com.hicham.wcstoreapp.models.PaymentMethod
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class NetworkCheckoutRepository @Inject constructor(
    private val wooCommerceApi: WooCommerceApi
) : CheckoutRepository {
    private val checkoutState = MutableSharedFlow<NetworkCheckout>(replay = 1)
    override val checkout: Flow<CheckoutData> = checkoutState
        .onSubscription {
            checkoutState.emit(wooCommerceApi.getCheckout())
        }
        .map { it.toDomainModel() }

    override suspend fun updatePaymentMethod(paymentMethod: PaymentMethod): Result<Unit> =
        runCatching {
            val currentCheckout = checkoutState.first()
            checkoutState.emit(currentCheckout.copy(paymentMethod = paymentMethod.value))
        }

    override suspend fun placeOrder(
        shippingAddress: Address,
        billingAddress: Address
    ): Result<Long> {
        val paymentMethod = checkoutState.first().paymentMethod
            .ifEmpty { error("Empty payment method") }
        return runCatchingNetworkErrors {
            wooCommerceApi.placeOrder(
                NetworkPlaceOrderRequest(
                    shippingAddress = shippingAddress.toNetworkAddress(),
                    billingAddress = billingAddress.toNetworkAddress()
                        .copy(email = "email@test.com"), // TODO remove this once the email is handled
                    paymentMethod = paymentMethod
                )
            ).orderID
        }
    }
}