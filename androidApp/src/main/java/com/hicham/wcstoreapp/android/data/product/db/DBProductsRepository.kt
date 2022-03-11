package com.hicham.wcstoreapp.android.data.product.db

import androidx.paging.ExperimentalPagingApi
import com.hicham.wcstoreapp.android.data.db.AppDatabase
import com.hicham.wcstoreapp.android.data.db.entities.toDomainModel
import com.hicham.wcstoreapp.data.api.WooCommerceApi
import com.hicham.wcstoreapp.data.api.toDomainModel
import com.hicham.wcstoreapp.data.product.ProductsRepository
import com.hicham.wcstoreapp.models.Category
import com.hicham.wcstoreapp.models.Product
import com.kuuurt.paging.multiplatform.Pager
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

private const val DEFAULT_PRODUCT_PAGE_SIZE = 5

@OptIn(ExperimentalPagingApi::class)
class DBProductsRepository @Inject constructor(
    private val database: AppDatabase,
    private val wooCommerceApi: WooCommerceApi
) : ProductsRepository {
    private val productDao = database.productDao()
    override fun getProductList(
        scope: CoroutineScope,
        query: String?,
        category: Category?
    ): Pager<Int, Product> {
        TODO("Not yet implemented")
    }

//    override fun getProductList(query: String?, category: Category?): Flow<PagingData<Product>> {
//        if (query == null && category == null) {
//            return Pager(
//                config = PagingConfig(DEFAULT_PRODUCT_PAGE_SIZE),
//                remoteMediator = ProductRemoteMediator(database, wooCommerceApi)
//            ) {
//                productDao.pagingSource()
//            }.flow
//                .map { pagingData ->
//                    pagingData.map { it.toDomainModel() }
//                }
//        } else {
//            // We don't support offline data when filtering by any
//            return Pager(
//                PagingConfig(pageSize = DEFAULT_PRODUCT_PAGE_SIZE)
//            ) {
//                NetworkProductsPagingSource(wooCommerceApi, database, query, category)
//            }.flow
//        }
//    }


    override suspend fun getProduct(id: Long): Product {
        return productDao.getProduct(id)?.toDomainModel() ?: wooCommerceApi.getProduct(id)
            .toDomainModel()
    }
}