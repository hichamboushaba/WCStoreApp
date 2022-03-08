package com.hicham.wcstoreapp.android.data.product

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.hicham.wcstoreapp.android.data.api.WooCommerceApi
import com.hicham.wcstoreapp.android.data.db.AppDatabase
import com.hicham.wcstoreapp.android.data.db.entities.ProductEntity
import com.hicham.wcstoreapp.android.data.db.entities.toEntity
import com.hicham.wcstoreapp.android.util.runCatchingNetworkErrors
import logcat.logcat

@ExperimentalPagingApi
class ProductRemoteMediator(
    private val database: AppDatabase,
    private val api: WooCommerceApi
) : RemoteMediator<Int, ProductEntity>() {
    private val productDao = database.productDao()

    // TODO confirm if this is OK, since documentation states the need of a separate table for storing the next key
    private var offset: Int = 0
    private val idsOfDeletedProducts = mutableListOf<Long>()

    override suspend fun initialize(): InitializeAction {
        // Start with list of all products
        idsOfDeletedProducts.addAll(productDao.getIdsOfProducts())
        return super.initialize()
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ProductEntity>
    ): MediatorResult {
        return runCatchingNetworkErrors {
            // Calculate the offset of next call
            offset = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> return@runCatchingNetworkErrors MediatorResult.Success(
                    endOfPaginationReached = true
                )
                LoadType.APPEND -> {
                    offset
                }
            }

            val pageSize = if (loadType == LoadType.REFRESH) {
                state.config.initialLoadSize
            } else {
                state.config.pageSize
            }

            logcat {
                "load products from API, offset = $offset, pageSize = $pageSize"
            }

            val products = api.getProducts(
                pageSize = pageSize,
                offset = offset
            ).map { it.toEntity() }

            logcat {
                "Received ${products.size} successfully"
            }
            // Keep track of offset
            this.offset += products.size

            val endOfPaginationReached = products.size < pageSize
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    productDao.clearAll()
                }
                productDao.insertProduct(*products.toTypedArray())
            }

            return@runCatchingNetworkErrors MediatorResult.Success(endOfPaginationReached)
        }.fold(
            onSuccess = { it },
            onFailure = {
                MediatorResult.Error(it)
            }
        )
    }
}