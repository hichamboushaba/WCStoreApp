package com.hicham.wcstoreapp.data.product.network

import com.hicham.wcstoreapp.data.api.WooCommerceApi
import com.hicham.wcstoreapp.data.api.toDomainModel
import com.hicham.wcstoreapp.data.product.ProductsRepository
import com.hicham.wcstoreapp.models.Category
import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.util.Inject
import com.hicham.wcstoreapp.util.runCatchingNetworkErrors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

private const val DEFAULT_PRODUCT_PAGE_SIZE = 5

class NetworkProductsRepository @Inject constructor(
    private val wooCommerceApi: WooCommerceApi
) : ProductsRepository {
    private val mutex = Mutex()

    private var currentQuery: String? = null
    private var currentCategory: Category? = null

    override val hasNext = MutableStateFlow(true)
    private var currentOffset = 0

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    override val products
        get() = _products

    override suspend fun fetch(query: String?, category: Category?): Result<Unit> {
        currentQuery = query
        currentCategory = category
        currentOffset = 0
        hasNext.value = true
        _products.value = emptyList()

        return loadNext()
    }

    override suspend fun loadNext(): Result<Unit> {
        return mutex.withLock {
            if (!hasNext.value) return@withLock Result.success(Unit)

            runCatchingNetworkErrors {
                val products = getProducts(currentQuery, currentCategory, currentOffset)
                hasNext.value = products.size == DEFAULT_PRODUCT_PAGE_SIZE
                currentOffset += products.size
                _products.update { it + products }
            }
        }
    }

    private suspend fun getProducts(
        query: String?,
        category: Category?,
        offset: Int,
    ): List<Product> {
        val response = wooCommerceApi.getProducts(
            pageSize = DEFAULT_PRODUCT_PAGE_SIZE,
            offset = offset,
            query = query,
            categoryId = category?.id?.toString()
        )

        return response.map { it.toDomainModel() }
    }

    override suspend fun getProduct(id: Long): Product {
        return wooCommerceApi.getProduct(id).toDomainModel()
    }
}