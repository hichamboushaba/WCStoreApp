package com.hicham.wcstoreapp.util

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal

inline fun <T> Iterable<T>.sumOf(selector: (T) -> BigDecimal): BigDecimal {
    var sum = 0.toBigDecimal()
    for (element in this) {
        sum += selector(element)
    }
    return sum
}
