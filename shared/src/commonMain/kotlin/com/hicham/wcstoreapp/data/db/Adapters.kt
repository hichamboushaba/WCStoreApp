package com.hicham.wcstoreapp.data.db

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.squareup.sqldelight.ColumnAdapter

val listOfStringsAdapter = object : ColumnAdapter<List<String>, String> {
    override fun decode(databaseValue: String) = databaseValue.split(",")
    override fun encode(value: List<String>) = value.joinToString(separator = ",")
}

val bigDecimalAdapter = object : ColumnAdapter<BigDecimal, String> {
    override fun decode(databaseValue: String): BigDecimal =
        databaseValue.takeIf { it.isNotEmpty() }?.let {
            BigDecimal.parseString(it)
        } ?: BigDecimal.ZERO

    override fun encode(value: BigDecimal): String = value.toPlainString()
}
