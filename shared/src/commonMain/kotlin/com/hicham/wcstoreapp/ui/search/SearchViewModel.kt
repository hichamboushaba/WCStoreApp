package com.hicham.wcstoreapp.ui.search

import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import com.hicham.wcstoreapp.android.data.cart.CartRepository
import com.hicham.wcstoreapp.data.currencyformat.CurrencyFormatProvider
import com.hicham.wcstoreapp.data.product.ProductsRepository
import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.ui.BaseViewModel
import com.hicham.wcstoreapp.ui.NavigationManager
import com.hicham.wcstoreapp.ui.navigation.Screen
import com.hicham.wcstoreapp.ui.products.mapToUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class SearchViewModel(
    private val repository: ProductsRepository,
    cartRepository: CartRepository,
    private val navigationManager: NavigationManager,
    currencyFormatProvider: CurrencyFormatProvider
) : BaseViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val products = _searchQuery
        .debounce(500L)
        .flatMapLatest { query ->
            return@flatMapLatest if (query.trim().length >= 3) {
                repository.getProductList(query = query)
                    .cachedIn(viewModelScope)
            } else {
                flowOf(PagingData.from(emptyList<Product>()))
            }
        }
        .mapToUiModel(this, currencyFormatProvider, cartRepository)
        .cachedIn(viewModelScope)
        .flowOn(Dispatchers.Default)

    fun onQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onProductClicked(product: Product) {
        val route = Screen.Product.createRoute(product.id)
        navigationManager.navigate(route)
    }
}