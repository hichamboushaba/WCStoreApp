package com.hicham.wcstoreapp.ui.home

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
import com.hicham.wcstoreapp.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ProductsRepository,
    private val currencyFormatProvider: CurrencyFormatProvider,
    private val cartRepository: CartRepository,
    private val navigationManager: NavigationManager
) : BaseViewModel() {
    private val currencyFormatter = currencyFormatProvider.formatSettings
        .map { CurrencyFormatter(it) }

    private val productPagingData = repository.getProductList().cachedIn(viewModelScope)

    val products: Flow<PagingData<ProductUiModel>> = combine(
        productPagingData,
        currencyFormatter,
        cartRepository.items
    ) { pagingData, formatter, cartItems ->
        pagingData.map { product ->
            ProductUiModel(
                product = product,
                priceFormatted = formatter.format(product.price),
                quantityInCart = cartItems.firstOrNull { it.product == product }?.quantity ?: 0
            )
        }
    }
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

data class ProductUiModel(
    val product: Product,
    val priceFormatted: String,
    val quantityInCart: Int = 0
)