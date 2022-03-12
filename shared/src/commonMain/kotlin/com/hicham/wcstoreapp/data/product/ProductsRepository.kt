package com.hicham.wcstoreapp.data.product

import com.hicham.wcstoreapp.models.Category
import com.hicham.wcstoreapp.models.Product
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
    val products: Flow<ProductsListState>
    suspend fun fetch(query: String? = null, category: Category? = null): Result<Unit>
    suspend fun loadNext(): Result<Unit>
    suspend fun getProduct(id: Long): Product
}

data class ProductsListState(
    val products: List<Product>,
    val hasNext: Boolean,
    val state: LoadingState
) {
    companion object {
        val empty: ProductsListState
            get() = ProductsListState(
                products = emptyList(),
                hasNext = true,
                state = LoadingState.Loading
            )
    }
}

enum class LoadingState {
    Loading, Success, AppendLoading, AppendError, Error
}