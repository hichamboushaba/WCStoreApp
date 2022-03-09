@file:UseSerializers(BigDecimalSerializer::class)

package com.hicham.wcstoreapp.android.data.api

import com.hicham.wcstoreapp.android.util.BigDecimalSerializer
import com.hicham.wcstoreapp.models.ProductPrices
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
data class NetworkProductPrices(
    val price: BigDecimal,

    @SerialName("regular_price")
    val regularPrice: BigDecimal,

    @SerialName("sale_price")
    val salePrice: BigDecimal
) : NetworkFormattable()

fun NetworkProductPrices.toDomainModel(): ProductPrices {
    return ProductPrices(
        price = calculatePrice(price),
        regularPrice = calculatePrice(regularPrice),
        salePrice = calculatePrice(salePrice),
    )
}