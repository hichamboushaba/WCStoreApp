package com.hicham.wcstoreapp.data.product.network

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.hicham.wcstoreapp.data.api.WooCommerceApi
import com.hicham.wcstoreapp.data.product.ProductsRepository
import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.models.toProduct
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

private const val DEFAULT_PRODUCT_PAGE_SIZE = 5

class NetworkProductsRepository @Inject constructor(
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