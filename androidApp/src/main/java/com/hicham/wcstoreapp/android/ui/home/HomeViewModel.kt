package com.hicham.wcstoreapp.android.ui.home

import androidx.lifecycle.viewModelScope
import com.hicham.wcstoreapp.android.data.cart.CartRepository
import com.hicham.wcstoreapp.android.ui.BaseViewModel
import com.hicham.wcstoreapp.android.ui.ShowSnackbar
import com.hicham.wcstoreapp.android.ui.navigation.Screen
import com.hicham.wcstoreapp.android.ui.products.mapToUiModel
import com.hicham.wcstoreapp.data.category.CategoryRepository
import com.hicham.wcstoreapp.data.currencyformat.CurrencyFormatProvider
import com.hicham.wcstoreapp.data.product.ProductsRepository
import com.hicham.wcstoreapp.models.Category
import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.ui.NavigationManager
import com.kuuurt.paging.multiplatform.helpers.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    repository: ProductsRepository,
    private val cartRepository: CartRepository,
    private val categoryRepository: CategoryRepository,
    private val navigationManager: NavigationManager,
    currencyFormatProvider: CurrencyFormatProvider
) : BaseViewModel() {
    companion object {
        val ALL_CATEGORY = Category(-1L, "All")
    }

    private val selectedCategory = MutableStateFlow(ALL_CATEGORY)

    val products = selectedCategory
        .flatMapLatest { category ->
            repository.getProductList(
                scope = viewModelScope,
                category = category.takeIf { it != ALL_CATEGORY }
            ).pagingData
        }
        .cachedIn(viewModelScope)
        .mapToUiModel(currencyFormatProvider, cartRepository)
        .cachedIn(viewModelScope)
        .flowOn(Dispatchers.Default)


    private val _categories = categoryRepository.categories.map { list ->
        list.toMutableList().apply { add(0, ALL_CATEGORY) }
    }
    val categories = combine(_categories, selectedCategory) { categories, selectedCategory ->
        categories.map {
            CategoryUiModel(category = it, isSelected = it == selectedCategory)
        }
    }

    init {
        viewModelScope.launch {
            categoryRepository.refresh()
        }
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

    fun onCategorySelected(category: Category) {
        selectedCategory.value = category
    }
}

data class CategoryUiModel(
    val category: Category,
    val isSelected: Boolean
)
