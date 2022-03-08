package com.hicham.wcstoreapp.android.ui.search

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hicham.wcstoreapp.android.data.cart.CartRepository
import com.hicham.wcstoreapp.android.data.currencyformat.CurrencyFormatProvider
import com.hicham.wcstoreapp.android.data.product.ProductsRepository
import com.hicham.wcstoreapp.android.models.Product
import com.hicham.wcstoreapp.android.ui.BaseViewModel
import com.hicham.wcstoreapp.android.ui.ShowSnackbar
import com.hicham.wcstoreapp.android.ui.navigation.NavigationManager
import com.hicham.wcstoreapp.android.ui.navigation.Screen
import com.hicham.wcstoreapp.android.ui.products.mapToUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: ProductsRepository,
    private val cartRepository: CartRepository,
    private val navigationManager: NavigationManager,
    currencyFormatProvider: CurrencyFormatProvider
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
            cartRepository.addItem(product).onFailure {
                triggerEffect(ShowSnackbar("Error while updating your cart"))
            }
        }
    }

    fun deleteItemFromCart(product: Product) {
        viewModelScope.launch {
            cartRepository.deleteItem(product).onFailure {
                triggerEffect(ShowSnackbar("Error while updating your cart"))
            }
        }
    }

    fun onProductClicked(product: Product) {
        val route = Screen.Product.createRoute(product.id)
        navigationManager.navigate(route)
    }
}