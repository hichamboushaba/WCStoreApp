package com.hicham.wcstoreapp.data.checkout.network

import com.hicham.wcstoreapp.data.api.NetworkCheckout
import com.hicham.wcstoreapp.data.api.WooCommerceApi
import com.hicham.wcstoreapp.data.api.toNetworkAddress
import com.hicham.wcstoreapp.data.checkout.CheckoutRepository
import com.hicham.wcstoreapp.models.Address
import com.hicham.wcstoreapp.models.CheckoutData
import com.hicham.wcstoreapp.models.PaymentMethod
import com.hicham.wcstoreapp.models.toDomainModel
import kotlinx.coroutines.flow.*
import logcat.LogPriority
import logcat.asLog
import logcat.logcat
import okio.IOException
import retrofit2.HttpException
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

    override suspend fun updateShippingAddress(shippingAddress: Address): Result<Unit> {
        val currentCheckout = checkoutState.first()
        return try {
            checkoutState.emit(
                wooCommerceApi.updateCheckout(
                    shippingAddress = shippingAddress.toNetworkAddress(),
                    paymentMethod = currentCheckout.paymentMethod
                )
            )
            Result.success(Unit)
        } catch (e: IOException) {
            logcat(LogPriority.WARN) { e.asLog() }
            Result.failure(e)
        } catch (e: HttpException) {
            logcat(LogPriority.WARN) { e.asLog() }
            Result.failure(e)
        }
    }

    override suspend fun updateBillingAddress(billingAddress: Address): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun updatePaymentMethod(paymentMethod: PaymentMethod): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun processCheckout(): Long {
        TODO("Not yet implemented")
    }
}