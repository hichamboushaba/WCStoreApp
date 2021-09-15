package com.hicham.wcstoreapp.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.hicham.wcstoreapp.models.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

private const val DEFAULT_PRODUCT_PAGE_SIZE = 5

interface ProductsRepository {
    fun getProductList(): Flow<PagingData<Product>>
}

class ProductsRepositoryImpl @Inject constructor(
    private val pagingSource: PagingSource<Int, Product>
) : ProductsRepository {
    override fun getProductList(): Flow<PagingData<Product>> {
        return Pager(
            PagingConfig(pageSize = DEFAULT_PRODUCT_PAGE_SIZE)
        ) {
            pagingSource
        }.flow
    }
}