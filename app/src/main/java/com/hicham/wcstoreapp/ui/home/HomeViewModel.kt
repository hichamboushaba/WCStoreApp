package com.hicham.wcstoreapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.hicham.wcstoreapp.data.Status
import com.hicham.wcstoreapp.data.source.ProductsRepository
import com.hicham.wcstoreapp.models.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: ProductsRepository) : ViewModel() {
    val products = repository.getProductList().cachedIn(viewModelScope)
        .map {
            it.map { product ->
                ProductUiModel(
                    id = product.id,
                    name = product.name,
                    priceFormatted = "10 USD",
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