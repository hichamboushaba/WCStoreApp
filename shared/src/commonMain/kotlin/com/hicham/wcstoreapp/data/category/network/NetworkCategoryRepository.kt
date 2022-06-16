package com.hicham.wcstoreapp.data.category.network

import com.hicham.wcstoreapp.data.storeApi.WooCommerceApi
import com.hicham.wcstoreapp.data.storeApi.toDomainModel
import com.hicham.wcstoreapp.data.category.CategoryRepository
import com.hicham.wcstoreapp.models.Category
import com.hicham.wcstoreapp.util.runCatchingNetworkErrors
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class NetworkCategoryRepository(private val wooCommerceApi: WooCommerceApi) : CategoryRepository {
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    override val categories: Flow<List<Category>>
        get() = _categories

    override suspend fun refresh() {
        runCatchingNetworkErrors { wooCommerceApi.getCategories() }
            .onSuccess { categories ->
                _categories.value = categories.map { it.toDomainModel() }
            }
    }
}