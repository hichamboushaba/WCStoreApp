package com.hicham.wcstoreapp.data.storeApi

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class BigDecimalSerializer : KSerializer<BigDecimal> {
    override fun deserialize(decoder: Decoder): BigDecimal {
        return decoder.decodeString().takeIf { it.isNotEmpty() }?.let {
            BigDecimal.parseString(it)
        } ?: BigDecimal.ZERO
    }

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("BigDecimal", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: BigDecimal) {
        encoder.encodeString(value.toPlainString())
    }
}