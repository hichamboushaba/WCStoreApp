package com.hicham.wcstoreapp.ui.home

import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    repository: ProductsRepository,
    currencyFormatProvider: CurrencyFormatProvider,
    private val cartRepository: CartRepository,
    private val navigationManager: NavigationManager
) : BaseViewModel() {
    val products = repository.getProductList()
        .cachedIn(viewModelScope)
        .mapToUiModel(currencyFormatProvider, cartRepository)
        .cachedIn(viewModelScope)
        .flowOn(Dispatchers.Default)

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
