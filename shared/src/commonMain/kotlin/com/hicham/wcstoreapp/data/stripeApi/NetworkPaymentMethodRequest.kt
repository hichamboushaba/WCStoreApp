package com.hicham.wcstoreapp.data.stripeApi

import com.hicham.wcstoreapp.models.Address
import com.hicham.wcstoreapp.models.PaymentCard

class NetworkPaymentMethodRequest(
    val billingAddress: Address,
    val card: PaymentCard
)
