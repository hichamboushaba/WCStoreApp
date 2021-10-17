package com.hicham.wcstoreapp.data.order.network

import com.hicham.wcstoreapp.data.api.LineItem
import com.hicham.wcstoreapp.data.api.NetworkAddress
import com.hicham.wcstoreapp.data.api.NetworkOrderCreationRequest
import com.hicham.wcstoreapp.data.api.WooCommerceApi
import com.hicham.wcstoreapp.data.order.OrderRepository
import com.hicham.wcstoreapp.models.Address
import com.hicham.wcstoreapp.models.CartItem
import com.hicham.wcstoreapp.models.PaymentMethod
import javax.inject.Inject

class NetworkOrderRepository @Inject constructor(
    private val api: WooCommerceApi
) : OrderRepository {
    override suspend fun createOrder(
        items: List<CartItem>,
        shippingAddress: Address,
        billingAddress: Address,
        paymentMethod: PaymentMethod
    ): Result<Long> {
        val request = NetworkOrderCreationRequest(
            paymentMethod = paymentMethod.id,
            setPaid = false,
            billing = billingAddress.toNetworkAddress(),
            shipping = shippingAddress.toNetworkAddress(),
            lineItems = items.toLineItems()
        )

        return runCatching { api.createOrder(request).id }
    }
}

private fun List<CartItem>.toLineItems(): List<LineItem> {
    return map {
        LineItem(
            productID = it.product.id,
            quantity = it.quantity,
            variationID = null // TODO
        )
    }
}

private val PaymentMethod.id: String
    get() = when (this) {
        PaymentMethod.WIRE -> "cod"
        PaymentMethod.CASH -> "bacs"
    }

private fun Address.toNetworkAddress(): NetworkAddress {
    return NetworkAddress(
        firstName = firstName,
        lastName = lastName,
        address1 = street1,
        address2 = street2,
        city = city,
        state = state,
        postcode = postCode,
        country = country
    )
}