package com.hicham.wcstoreapp.data.product

import com.hicham.wcstoreapp.models.Category
import com.hicham.wcstoreapp.models.Product
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
    val products: Flow<List<Product>>
    val hasNext: Flow<Boolean>
    suspend fun fetch(query: String? = null, category: Category? = null): Result<Unit>
    suspend fun loadNext(): Result<Unit>
    suspend fun getProduct(id: Long): Product
}