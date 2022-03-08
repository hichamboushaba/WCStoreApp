package com.hicham.wcstoreapp.android.data.product

import androidx.paging.PagingData
import com.hicham.wcstoreapp.models.Category
import com.hicham.wcstoreapp.models.Product
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
    fun getProductList(query: String? = null, category: Category? = null): Flow<PagingData<Product>>
    suspend fun getProduct(id: Long): Product
}