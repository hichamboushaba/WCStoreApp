package com.hicham.wcstoreapp.data.product.network

import app.cash.paging.*
import com.hicham.wcstoreapp.data.db.daos.ProductDao
import com.hicham.wcstoreapp.data.db.toEntity
import com.hicham.wcstoreapp.data.storeApi.WooCommerceApi
import com.hicham.wcstoreapp.data.storeApi.toDomainModel
import com.hicham.wcstoreapp.models.Category
import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.util.DB
import com.hicham.wcstoreapp.util.LogPriority
import com.hicham.wcstoreapp.util.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NetworkProductsPagingSource(
    private val api: WooCommerceApi,
    private val productDao: ProductDao,
    private val query: String?,
    private val category: Category?
) : PagingSource<Int, Product>() {
    override fun getRefreshKey(state: PagingState<Int, Product>): Int? = null

    override suspend fun load(params: PagingSourceLoadParams<Int>): PagingSourceLoadResult<Int, Product> {
        return try {
            // Start refresh at offset 0 if undefined.
            val offset = params.key ?: 0
            val response = api.getProducts(
                pageSize = params.loadSize,
                offset = offset,
                query = query,
                categoryId = category?.id?.toString()
            )

            val products = response.map { it.toDomainModel() }

            cacheProducts(products)

            PagingSourceLoadResultPage(
                data = products,
                prevKey = null, // Only paging forward.
                nextKey = if (response.size < params.loadSize) null else offset + response.size
            )
        } catch (e: Exception) {
            log(priority = LogPriority.WARN) {
                e.toString()
            }
            PagingSourceLoadResultError(e)
        }
    }

    private suspend fun cacheProducts(products: List<Product>) = withContext(Dispatchers.DB) {
        productDao.transaction {
            productDao.upsertProducts(*products.map { it.toEntity() }.toTypedArray())
        }
    }
}