package com.hicham.wcstoreapp.data.product.db

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import app.cash.paging.map
import app.cash.sqldelight.paging3.QueryPagingSource
import com.hicham.wcstoreapp.data.db.ProductEntity
import com.hicham.wcstoreapp.data.db.daos.ProductDao
import com.hicham.wcstoreapp.data.db.toDomainModel
import com.hicham.wcstoreapp.data.product.ProductRemoteMediator
import com.hicham.wcstoreapp.data.product.ProductsRepository
import com.hicham.wcstoreapp.data.product.network.NetworkProductsPagingSource
import com.hicham.wcstoreapp.data.storeApi.WooCommerceApi
import com.hicham.wcstoreapp.data.storeApi.toDomainModel
import com.hicham.wcstoreapp.models.Category
import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.util.DB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DEFAULT_PRODUCT_PAGE_SIZE = 10

class DBProductsRepository(
    private val wooCommerceApi: WooCommerceApi,
    private val productDao: ProductDao
) : ProductsRepository {
    override fun getProductList(query: String?, category: Category?): Flow<PagingData<Product>> {
        if (query == null && category == null) {
            return Pager(
                config = PagingConfig(DEFAULT_PRODUCT_PAGE_SIZE, maxSize = 3*DEFAULT_PRODUCT_PAGE_SIZE),
                remoteMediator = ProductRemoteMediator(productDao, wooCommerceApi)
            ) { pagingSource() }
                .flow
                .map { pagingData: PagingData<ProductEntity> ->
                    pagingData.map { it.toDomainModel() }
                }
        } else {
            // We don't support offline data when filtering by any
            return Pager(
                PagingConfig(pageSize = DEFAULT_PRODUCT_PAGE_SIZE)
            ) {
                NetworkProductsPagingSource(wooCommerceApi, productDao, query, category)
            }.flow
        }
    }

    override suspend fun getProduct(id: Long): Product {
        return wooCommerceApi.getProduct(id).toDomainModel()
    }

    private fun pagingSource() = QueryPagingSource(
        countQuery = productDao.count(),
        transacter = productDao,
        context = Dispatchers.DB,
        queryProvider = productDao::getProducts,
    )
}