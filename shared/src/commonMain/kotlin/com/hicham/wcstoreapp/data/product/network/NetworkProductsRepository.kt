package com.hicham.wcstoreapp.data.product.network

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import com.hicham.wcstoreapp.data.db.daos.ProductDao
import com.hicham.wcstoreapp.data.product.ProductsRepository
import com.hicham.wcstoreapp.data.storeApi.WooCommerceApi
import com.hicham.wcstoreapp.data.storeApi.toDomainModel
import com.hicham.wcstoreapp.models.Category
import com.hicham.wcstoreapp.models.Product
import kotlinx.coroutines.flow.Flow

private const val DEFAULT_PRODUCT_PAGE_SIZE = 5

class NetworkProductsRepository(
    private val wooCommerceApi: WooCommerceApi,
    private val productDao: ProductDao
) : ProductsRepository {
    override fun getProductList(query: String?, category: Category?): Flow<PagingData<Product>> {
        return Pager(
            PagingConfig(pageSize = DEFAULT_PRODUCT_PAGE_SIZE)
        ) {
            NetworkProductsPagingSource(wooCommerceApi, productDao, query, category)
        }.flow
    }

    override suspend fun getProduct(id: Long): Product {
        return wooCommerceApi.getProduct(id).toDomainModel()
    }
}