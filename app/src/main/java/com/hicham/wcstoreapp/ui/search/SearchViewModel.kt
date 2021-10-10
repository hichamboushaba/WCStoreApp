package com.hicham.wcstoreapp.ui.search

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.hicham.wcstoreapp.data.cart.CartRepository
import com.hicham.wcstoreapp.data.currencyformat.CurrencyFormatProvider
import com.hicham.wcstoreapp.data.product.ProductsRepository
import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.ui.BaseViewModel
import com.hicham.wcstoreapp.ui.CurrencyFormatter
import com.hicham.wcstoreapp.ui.navigation.NavigationManager
import com.hicham.wcstoreapp.ui.products.ProductUiModel
import com.hicham.wcstoreapp.ui.products.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: ProductsRepository,
    private val currencyFormatProvider: CurrencyFormatProvider,
    private val cartRepository: CartRepository,
    private val navigationManager: NavigationManager
) : BaseViewModel() {
    private val currencyFormatter = currencyFormatProvider.formatSettings
        .map { CurrencyFormatter(it) }

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val productPagingData = _searchQuery
        .flatMapLatest { query ->
            // debounce using a simple delay
            delay(500L)
            return@flatMapLatest if (query.trim().length >= 3) {
                repository.getProductList(query = query)
                    .cachedIn(viewModelScope)
            } else {
                flowOf(PagingData.from(emptyList<Product>()))
            }
        }

    val products: Flow<PagingData<ProductUiModel>> = combine(
        productPagingData,
        currencyFormatter,
        cartRepository.items
    ) { pagingData, formatter, cartItems ->
        pagingData.map { product -> product.toUiModel(formatter, cartItems) }
    }
        .cachedIn(viewModelScope)
        .flowOn(Dispatchers.Default)

    fun onQueryChanged(query: String) {
        _searchQuery.value = query
    }
}