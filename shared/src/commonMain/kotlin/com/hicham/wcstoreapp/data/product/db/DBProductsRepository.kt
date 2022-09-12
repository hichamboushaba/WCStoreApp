package com.hicham.wcstoreapp.data.product.db

import com.hicham.wcstoreapp.data.storeApi.WooCommerceApi
import com.hicham.wcstoreapp.data.storeApi.toDomainModel
import com.hicham.wcstoreapp.data.db.daos.ProductDao
import com.hicham.wcstoreapp.data.db.toDomainModel
import com.hicham.wcstoreapp.data.db.toEntity
import com.hicham.wcstoreapp.data.product.LoadingState
import com.hicham.wcstoreapp.data.product.ProductsListState
import com.hicham.wcstoreapp.data.product.ProductsRepository
import com.hicham.wcstoreapp.models.Category
import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.util.DB
import com.hicham.wcstoreapp.util.runCatchingNetworkErrors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

private const val DEFAULT_PRODUCT_PAGE_SIZE = 5

class DBProductsRepository(
    private val wooCommerceApi: WooCommerceApi,
    private val productDao: ProductDao
) : ProductsRepository {
    private val mutex = Mutex()

    private var currentQuery = MutableStateFlow<String?>(null)
    private var currentCategory = MutableStateFlow<Category?>(null)

    private var currentOffset = 0

    private val searchResults = MutableStateFlow(emptyList<Product>())
    private val paginationState = MutableStateFlow(Pair(false, LoadingState.Success))

    override val products = combine(currentQuery, currentCategory) { query, category ->
        Pair(query, category)
    }.transformLatest { (query, category) ->
        if (!query.isNullOrEmpty() || category != null) {
            emitAll(searchResults)
        } else {
            emitAll(
                productDao.observeProducts()
                    .map { list -> list.map { it.toDomainModel() } }
            )
        }
    }.combine(paginationState) { results, (hasNext, state) ->
        ProductsListState(
            products = results,
            hasNext = hasNext,
            state = state
        )
    }

    override suspend fun fetch(query: String?, category: Category?): Result<Unit> {
        currentQuery.value = query
        currentCategory.value = category
        currentOffset = 0
        paginationState.value = Pair(true, LoadingState.Success)
        searchResults.value = emptyList()

        return loadNext()
    }

    override suspend fun loadNext(): Result<Unit> {
        return mutex.withLock {
            if (!paginationState.value.first) return@withLock Result.success(Unit)

            runCatchingNetworkErrors {
                paginationState.update {
                    it.copy(second = if (currentOffset == 0) LoadingState.Loading else LoadingState.AppendLoading)
                }
                val products = getProducts(currentQuery.value, currentCategory.value, currentOffset)

                cacheProducts(products)

                currentOffset += products.size
                paginationState.update {
                    it.copy(
                        first = products.size == DEFAULT_PRODUCT_PAGE_SIZE,
                        second = LoadingState.Success
                    )
                }

                if (!currentQuery.value.isNullOrEmpty() || currentCategory.value != null) {
                    searchResults.update { it + products }
                }
            }.onFailure {
                paginationState.update {
                    it.copy(second = if (currentOffset == 0) LoadingState.Error else LoadingState.AppendError)
                }
            }
        }
    }

    private suspend fun cacheProducts(products: List<Product>) = withContext(Dispatchers.DB) {
        productDao.transaction {
            if (currentOffset == 0 && currentQuery.value.isNullOrEmpty() && currentCategory.value == null) {
                productDao.deleteAll()
            }
            productDao.upsertProducts(*products.map { it.toEntity() }.toTypedArray())
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