package com.hicham.wcstoreapp.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.hicham.wcstoreapp.data.source.network.WooCommerceApi
import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.models.toProduct
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

private const val DEFAULT_PRODUCT_PAGE_SIZE = 5

interface ProductsRepository {
    fun getProductList(): Flow<PagingData<Product>>
    suspend fun getProduct(id: Long): Product
}

class ProductsRepositoryImpl @Inject constructor(
    private val pagingSource: PagingSource<Int, Product>,
    private val wooCommerceApi: WooCommerceApi
) : ProductsRepository {
    override fun getProductList(): Flow<PagingData<Product>> {
        return Pager(
            PagingConfig(pageSize = DEFAULT_PRODUCT_PAGE_SIZE)
        ) {
            pagingSource
        }.flow
    }

    override suspend fun getProduct(id: Long): Product {
        return wooCommerceApi.getProduct(id).toProduct()
    }
}