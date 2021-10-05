package com.hicham.wcstoreapp.data.product

import androidx.paging.PagingData
import com.hicham.wcstoreapp.models.Product
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
    fun getProductList(): Flow<PagingData<Product>>
    suspend fun getProduct(id: Long): Product
}