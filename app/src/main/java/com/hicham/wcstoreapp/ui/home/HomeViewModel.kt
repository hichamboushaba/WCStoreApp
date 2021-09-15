package com.hicham.wcstoreapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.hicham.wcstoreapp.data.CurrencyFormatProvider
import com.hicham.wcstoreapp.data.ProductsRepository
import com.hicham.wcstoreapp.ui.CurrencyFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ProductsRepository,
    private val currencyFormatProvider: CurrencyFormatProvider
) : ViewModel() {
    private val currencyFormatter = currencyFormatProvider.formatSettings
        .map { CurrencyFormatter(it) }

    val products = repository.getProductList().cachedIn(viewModelScope)
        .combine(currencyFormatter) { pagingData, formatter ->
            Pair(pagingData, formatter)
        }
        .map { (pagingData, formatter) ->
            pagingData.map { product ->
                ProductUiModel(
                    id = product.id,
                    name = product.name,
                    priceFormatted = formatter.format(product.price),
                    images = product.images
                )
            }
        }
}

data class ProductUiModel(
    val id: Long,
    val name: String,
    val priceFormatted: String,
    val images: List<String>,
    val quantityInCart: Int = 0
)