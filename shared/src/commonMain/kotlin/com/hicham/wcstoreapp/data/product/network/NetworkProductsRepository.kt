package com.hicham.wcstoreapp.data.product.network

import com.hicham.wcstoreapp.data.api.WooCommerceApi
import com.hicham.wcstoreapp.data.api.toDomainModel
import com.hicham.wcstoreapp.data.product.ProductsRepository
import com.hicham.wcstoreapp.models.Category
import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.util.Inject
import com.kuuurt.paging.multiplatform.Pager
import com.kuuurt.paging.multiplatform.PagingConfig
import com.kuuurt.paging.multiplatform.PagingResult
import kotlinx.coroutines.CoroutineScope

private const val DEFAULT_PRODUCT_PAGE_SIZE = 5

class NetworkProductsRepository @Inject constructor(
    private val wooCommerceApi: WooCommerceApi
) : ProductsRepository {
    override fun getProductList(
        scope: CoroutineScope,
        query: String?,
        category: Category?
    ): Pager<Int, Product> {
        return Pager(
            config = PagingConfig(pageSize = DEFAULT_PRODUCT_PAGE_SIZE),
            clientScope = scope,
            initialKey = 0,
            getItems = { key, pageSize -> getProducts(query, category, key, pageSize) }
        )
    }

    private suspend fun getProducts(
        query: String?,
        category: Category?,
        offset: Int,
        pageSize: Int
    ): PagingResult<Int, Product> {
        val response = wooCommerceApi.getProducts(
            pageSize = pageSize,
            offset = offset,
            query = query,
            categoryId = category?.id?.toString()
        )

        return PagingResult(
            items = response.map { it.toDomainModel() },
            currentKey = offset,
            prevKey = { null }, // Only paging forward.
            nextKey = { if (response.size < pageSize) null else offset + response.size }
        )
    }

    override suspend fun getProduct(id: Long): Product {
        return wooCommerceApi.getProduct(id).toDomainModel()
    }
}