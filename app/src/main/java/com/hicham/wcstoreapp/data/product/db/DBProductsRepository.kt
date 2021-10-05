package com.hicham.wcstoreapp.data.product.db

import androidx.paging.*
import com.hicham.wcstoreapp.data.api.WooCommerceApi
import com.hicham.wcstoreapp.data.db.AppDatabase
import com.hicham.wcstoreapp.data.product.ProductRemoteMediator
import com.hicham.wcstoreapp.data.product.ProductsRepository
import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.models.toProduct
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val DEFAULT_PRODUCT_PAGE_SIZE = 10

@OptIn(ExperimentalPagingApi::class)
class DBProductsRepository @Inject constructor(
    private val database: AppDatabase,
    private val api: WooCommerceApi
) : ProductsRepository {
    private val productDao = database.productDao()

    override fun getProductList(): Flow<PagingData<Product>> = Pager(
        config = PagingConfig(DEFAULT_PRODUCT_PAGE_SIZE),
        remoteMediator = ProductRemoteMediator(database, api)
    ) {
        productDao.pagingSource()
    }.flow.map { pagingData ->
        pagingData.map { it.toProduct() }
    }

    override suspend fun getProduct(id: Long): Product {
        return productDao.getProduct(id)?.toProduct() ?: api.getProduct(id).toProduct()
    }
}