package com.hicham.wcstoreapp.data

import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.models.ProductPrices
import java.math.BigDecimal

object Fakes {
    val prices = ProductPrices(
        price = BigDecimal.TEN,
        salePrice = BigDecimal.ONE,
        regularPrice = BigDecimal.TEN
    )

    val product = Product(
        id = 0L,
        name = "product",
        images = listOf("https://i0.wp.com/hichamwootest.wpcomstaging.com/wp-content/uploads/2020/08/logo-1.jpg?fit=800%2C799&ssl=1"),
        prices = prices,
        shortDescription = "",
        description = ""
    )
}
