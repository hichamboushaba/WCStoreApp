package com.hicham.wcstoreapp.data.source.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hicham.wcstoreapp.data.source.db.AppDatabase
import com.hicham.wcstoreapp.data.source.db.entities.ProductEntity
import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.models.toProduct
import kotlinx.coroutines.delay
import logcat.LogPriority
import logcat.logcat
import javax.inject.Inject

class ProductsPagingSource @Inject constructor(
    private val api: WooCommerceApi,
    private val database: AppDatabase
) :
    PagingSource<Int, Product>() {
    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        return try {
            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key ?: 1
            delay(1000)
            val response = api.getProducts(pageSize = params.loadSize, page = nextPageNumber)

            cacheProducts(response)

            LoadResult.Page(
                data = response.map { it.toProduct() },
                prevKey = null, // Only paging forward.
                nextKey = if (response.size < params.loadSize) null else nextPageNumber + 1
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
            ProductEntity(
                id = it.id,
                name = it.name,
                images = it.images.map { it.src },
                price = it.price.toPlainString(),
                shortDescription = it.shortDescription,
                description = it.description
            )
        }.toTypedArray())
    }
}