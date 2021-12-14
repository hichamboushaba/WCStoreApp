package com.hicham.wcstoreapp.data

import com.hicham.wcstoreapp.models.Prices
import com.hicham.wcstoreapp.models.Product
import java.math.BigDecimal

object Fakes {
    val prices = Prices(
        price = BigDecimal.TEN,
        salePrice = BigDecimal.ONE,
        regularPrice = BigDecimal.TEN,
        formattedPrice = "10$",
        formattedSalePrice = "1$",
        formattedRegularPrice = "10$"
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
