package com.hicham.wcstoreapp.data.product.network

import com.hicham.wcstoreapp.data.api.WooCommerceApi
import com.hicham.wcstoreapp.data.api.toDomainModel
import com.hicham.wcstoreapp.data.product.LoadingState
import com.hicham.wcstoreapp.data.product.ProductsListState
import com.hicham.wcstoreapp.data.product.ProductsRepository
import com.hicham.wcstoreapp.models.Category
import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.util.runCatchingNetworkErrors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

private const val DEFAULT_PRODUCT_PAGE_SIZE = 5

class NetworkProductsRepository  constructor(
    private val wooCommerceApi: WooCommerceApi
) : ProductsRepository {
    private val mutex = Mutex()

    private var currentQuery: String? = null
    private var currentCategory: Category? = null

    private var currentOffset = 0

    private val _products = MutableStateFlow(ProductsListState.empty)
    override val products
        get() = _products

    override suspend fun fetch(query: String?, category: Category?): Result<Unit> {
        currentQuery = query
        currentCategory = category
        currentOffset = 0
        _products.value = ProductsListState.empty

        return loadNext()
    }

    override suspend fun loadNext(): Result<Unit> {
        return mutex.withLock {
            if (!_products.value.hasNext) return@withLock Result.success(Unit)

            runCatchingNetworkErrors {
                _products.update {
                    it.copy(state = if (currentOffset == 0) LoadingState.Loading else LoadingState.AppendLoading)
                }
                val products = getProducts(currentQuery, currentCategory, currentOffset)
                currentOffset += products.size
                _products.update {
                    it.copy(
                        products = it.products + products,
                        hasNext = products.size == DEFAULT_PRODUCT_PAGE_SIZE,
                        state = LoadingState.Success
                    )
                }
            }.onFailure {
                _products.update {
                    it.copy(state = if (currentOffset == 0) LoadingState.Error else LoadingState.AppendError)
                }
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