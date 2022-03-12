package com.hicham.wcstoreapp.android.ui.search

import com.hicham.wcstoreapp.android.data.cart.CartRepository
import com.hicham.wcstoreapp.android.ui.navigation.AndroidNavigationManager
import com.hicham.wcstoreapp.android.ui.navigation.Screen
import com.hicham.wcstoreapp.data.currencyformat.CurrencyFormatProvider
import com.hicham.wcstoreapp.data.product.ProductsRepository
import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.ui.BaseViewModel
import com.hicham.wcstoreapp.ui.ShowSnackbar
import com.hicham.wcstoreapp.ui.products.mapToUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: ProductsRepository,
    private val cartRepository: CartRepository,
    private val navigationManager: AndroidNavigationManager,
    currencyFormatProvider: CurrencyFormatProvider
) : BaseViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val products = repository.products
        .mapToUiModel(currencyFormatProvider, cartRepository)
        .flowOn(Dispatchers.Default)

    init {
        _searchQuery
            .debounce(500L)
            .onEach { repository.fetch(query = it) }
            .launchIn(viewModelScope)
    }

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

    fun loadNext() {
        viewModelScope.launch {
            repository.loadNext()
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