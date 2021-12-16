@file:UseSerializers(BigDecimalSerializer::class)

package com.hicham.wcstoreapp.data.api

import com.hicham.wcstoreapp.util.BigDecimalSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.math.BigDecimal

@Serializable
data class NetworkProductPrices(
    val price: BigDecimal,

    @SerialName("regular_price")
    val regularPrice: BigDecimal,

    @SerialName("sale_price")
    val salePrice: BigDecimal
) : NetworkFormattable()
