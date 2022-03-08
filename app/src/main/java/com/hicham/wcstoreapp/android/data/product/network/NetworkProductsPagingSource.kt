package com.hicham.wcstoreapp.android.data.product.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hicham.wcstoreapp.android.data.api.NetworkProduct
import com.hicham.wcstoreapp.android.data.api.WooCommerceApi
import com.hicham.wcstoreapp.android.data.api.toDomainModel
import com.hicham.wcstoreapp.android.data.db.AppDatabase
import com.hicham.wcstoreapp.android.data.db.entities.toEntity
import com.hicham.wcstoreapp.models.Category
import com.hicham.wcstoreapp.models.Product
import logcat.LogPriority
import logcat.logcat

class NetworkProductsPagingSource(
    private val api: WooCommerceApi,
    private val database: AppDatabase,
    private val query: String?,
    private val category: Category?
) : PagingSource<Int, Product>() {
    override fun getRefreshKey(state: PagingState<Int, Product>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        return try {
            // Start refresh at offset 0 if undefined.
            val offset = params.key ?: 0
            val response = api.getProducts(
                pageSize = params.loadSize,
                offset = offset,
                query = query,
                categoryId = category?.id?.toString()
            )

            cacheProducts(response)

            LoadResult.Page(
                data = response.map { it.toDomainModel() },
                prevKey = null, // Only paging forward.
                nextKey = if (response.size < params.loadSize) null else offset + response.size
            )
        } catch (e: Exception) {
            logcat(priority = LogPriority.WARN) {
                e.toString()
            }
            LoadResult.Error(e)
        }
    }

    private suspend fun cacheProducts(products: List<NetworkProduct>) {
        database.productDao().insertProduct(*products.map {
            it.toEntity()
        }.toTypedArray())
    }
}