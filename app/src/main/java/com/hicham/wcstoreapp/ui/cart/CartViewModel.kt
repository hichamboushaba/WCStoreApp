package com.hicham.wcstoreapp.ui.cart

import androidx.lifecycle.ViewModel
import com.hicham.wcstoreapp.data.CartRepository
import com.hicham.wcstoreapp.data.CurrencyFormatProvider
import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.ui.CurrencyFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val currencyFormatProvider: CurrencyFormatProvider
) : ViewModel() {
    val items =
        combine(
            cartRepository.items,
            currencyFormatProvider.formatSettings
        ) { products, formatSettings ->
            Pair(products, CurrencyFormatter(formatSettings))
        }.map { (products, currencyFormatter) ->
            products.groupBy { it }.map { (product, list) ->
                val quantity = list.size
                CartItemUiModel(
                    product = product,
                    quantity = quantity,
                    totalPriceFormatted = currencyFormatter.format(product.price.multiply(quantity.toBigDecimal()))
                )
            }
        }

    data class CartItemUiModel(
        val product: Product,
        val quantity: Int,
        val totalPriceFormatted: String
    )
}