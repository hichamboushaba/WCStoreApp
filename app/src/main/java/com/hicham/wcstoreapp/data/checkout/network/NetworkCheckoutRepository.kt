package com.hicham.wcstoreapp.data.checkout.network

import com.hicham.wcstoreapp.data.api.NetworkCheckout
import com.hicham.wcstoreapp.data.api.NetworkPlaceOrderRequest
import com.hicham.wcstoreapp.data.api.WooCommerceApi
import com.hicham.wcstoreapp.data.api.toNetworkAddress
import com.hicham.wcstoreapp.data.checkout.CheckoutRepository
import com.hicham.wcstoreapp.models.Address
import com.hicham.wcstoreapp.models.CheckoutData
import com.hicham.wcstoreapp.models.PaymentMethod
import com.hicham.wcstoreapp.models.toDomainModel
import com.hicham.wcstoreapp.util.runCatchingNetworkErrors
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
        .map {
            it.toDomainModel()
        }

    override suspend fun updatePaymentMethod(paymentMethod: PaymentMethod): Result<Unit> =
        runCatchingNetworkErrors {
            checkoutState.emit(
                wooCommerceApi.updateCheckout(
                    paymentMethod = paymentMethod.value
                )
            )
        }

    override suspend fun placeOrder(
        shippingAddress: Address,
        billingAddress: Address
    ): Result<Long> {
        val paymentMethod = checkoutState.first().paymentMethod
        return runCatchingNetworkErrors {
            wooCommerceApi.placeOrder(
                NetworkPlaceOrderRequest(
                    shippingAddress = shippingAddress.toNetworkAddress(),
                    billingAddress = billingAddress.toNetworkAddress(),
                    paymentMethod = paymentMethod
                )
            ).orderID
        }
    }
}