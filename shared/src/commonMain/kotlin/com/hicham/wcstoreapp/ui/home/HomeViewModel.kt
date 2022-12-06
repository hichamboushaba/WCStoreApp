package com.hicham.wcstoreapp.ui.home

import app.cash.paging.cachedIn
import com.hicham.wcstoreapp.android.data.cart.CartRepository
import com.hicham.wcstoreapp.data.category.CategoryRepository
import com.hicham.wcstoreapp.data.currencyformat.CurrencyFormatProvider
import com.hicham.wcstoreapp.data.product.ProductsRepository
import com.hicham.wcstoreapp.models.Category
import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.ui.BaseViewModel
import com.hicham.wcstoreapp.ui.NavigationManager
import com.hicham.wcstoreapp.ui.navigation.Screen
import com.hicham.wcstoreapp.ui.products.mapToUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel constructor(
    private val repository: ProductsRepository,
    cartRepository: CartRepository,
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
            repository.getProductList(category = category.takeIf { it != ALL_CATEGORY })
        }
        .cachedIn(viewModelScope)
        .mapToUiModel(this, currencyFormatProvider, cartRepository)
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
