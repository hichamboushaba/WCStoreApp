package com.hicham.wcstoreapp.data.product

import com.hicham.wcstoreapp.models.Category
import com.hicham.wcstoreapp.models.Product
import com.kuuurt.paging.multiplatform.PagingData
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
    fun getProductList(query: String? = null, category: Category? = null): Flow<PagingData<Product>>
    suspend fun getProduct(id: Long): Product
}