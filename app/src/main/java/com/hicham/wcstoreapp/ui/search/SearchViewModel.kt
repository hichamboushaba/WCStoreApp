package com.hicham.wcstoreapp.ui.search

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hicham.wcstoreapp.data.cart.CartRepository
import com.hicham.wcstoreapp.data.currencyformat.CurrencyFormatProvider
import com.hicham.wcstoreapp.data.product.ProductsRepository
import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.ui.BaseViewModel
import com.hicham.wcstoreapp.ui.navigation.NavigationManager
import com.hicham.wcstoreapp.ui.navigation.Screen
import com.hicham.wcstoreapp.ui.products.mapToUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: ProductsRepository,
    currencyFormatProvider: CurrencyFormatProvider,
    private val cartRepository: CartRepository,
    private val navigationManager: NavigationManager
) : BaseViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val products = _searchQuery
        .flatMapLatest { query ->
            // debounce using a simple delay
            delay(500L)
            return@flatMapLatest if (query.trim().length >= 3) {
                repository.getProductList(query = query)
                    .cachedIn(viewModelScope)
            } else {
                flowOf(PagingData.from(emptyList<Product>()))
            }
        }.mapToUiModel(currencyFormatProvider, cartRepository)
        .cachedIn(viewModelScope)
        .flowOn(Dispatchers.Default)

    fun onQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun addItemToCart(product: Product) {
        viewModelScope.launch {
            cartRepository.addItem(product)
        }
    }

    fun deleteItemFromCart(product: Product) {
        viewModelScope.launch {
            cartRepository.deleteItem(product)
        }
    }

    fun onProductClicked(id: Long) {
        val route = Screen.Product.createRoute(id)
        navigationManager.navigate(route)
    }
}