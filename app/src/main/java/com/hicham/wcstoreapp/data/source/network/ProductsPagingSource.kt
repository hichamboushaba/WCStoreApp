package com.hicham.wcstoreapp.data.source.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.models.toProduct
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ProductsPagingSource @Inject constructor(private val api: WooCommerceApi) :
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
            val response = api.getProducts(pageSize = params.loadSize, page = nextPageNumber)
            LoadResult.Page(
                data = response.map { it.toProduct() },
                prevKey = null, // Only paging forward.
                nextKey = nextPageNumber + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}