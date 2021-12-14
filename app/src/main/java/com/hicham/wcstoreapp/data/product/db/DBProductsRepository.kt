package com.hicham.wcstoreapp.data.product.db

import androidx.paging.*
import com.hicham.wcstoreapp.data.api.WooCommerceApi
import com.hicham.wcstoreapp.data.db.AppDatabase
import com.hicham.wcstoreapp.data.product.ProductRemoteMediator
import com.hicham.wcstoreapp.data.product.ProductsRepository
import com.hicham.wcstoreapp.data.product.network.NetworkProductsPagingSource
import com.hicham.wcstoreapp.models.Category
import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.models.toDomainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val DEFAULT_PRODUCT_PAGE_SIZE = 5

@OptIn(ExperimentalPagingApi::class)
class DBProductsRepository @Inject constructor(
    private val database: AppDatabase,
    private val wooCommerceApi: WooCommerceApi
) : ProductsRepository {
    private val productDao = database.productDao()

    override fun getProductList(query: String?, category: Category?): Flow<PagingData<Product>> {
        if (query == null && category == null) {
            return Pager(
                config = PagingConfig(DEFAULT_PRODUCT_PAGE_SIZE),
                remoteMediator = ProductRemoteMediator(database, wooCommerceApi)
            ) {
                productDao.pagingSource()
            }.flow
                .map { pagingData ->
                    pagingData.map {
                        it.toDomainModel()
                    }
                }
        } else {
            // We don't support offline data when filtering by any
            return Pager(
                PagingConfig(pageSize = DEFAULT_PRODUCT_PAGE_SIZE)
            ) {
                NetworkProductsPagingSource(wooCommerceApi, database, query, category)
            }.flow
        }
    }


    override suspend fun getProduct(id: Long): Product {
        return productDao.getProduct(id)?.toDomainModel() ?: wooCommerceApi.getProduct(id)
            .toDomainModel()
    }
}