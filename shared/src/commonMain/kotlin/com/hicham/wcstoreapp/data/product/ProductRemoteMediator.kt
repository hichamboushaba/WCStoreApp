package com.hicham.wcstoreapp.data.product

import app.cash.paging.*
import com.hicham.wcstoreapp.data.db.ProductEntity
import com.hicham.wcstoreapp.data.db.daos.ProductDao
import com.hicham.wcstoreapp.data.db.toEntity
import com.hicham.wcstoreapp.data.storeApi.WooCommerceApi
import com.hicham.wcstoreapp.data.storeApi.toDomainModel
import com.hicham.wcstoreapp.util.DB
import com.hicham.wcstoreapp.util.KtorNetworkException
import com.hicham.wcstoreapp.util.log
import io.ktor.utils.io.errors.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPagingApi::class)
class ProductRemoteMediator(
    private val productDao: ProductDao,
    private val api: WooCommerceApi
) : RemoteMediator<Int, ProductEntity>() {
    private var offset: Int = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ProductEntity>
    ): RemoteMediatorMediatorResult {
        try {
            // Calculate the offset of next call
            offset = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> return RemoteMediatorMediatorResultSuccess(
                    endOfPaginationReached = true
                )
                LoadType.APPEND -> {
                    offset
                }
                else -> {
                    0
                }
            }

            val pageSize = if (loadType == LoadType.REFRESH) {
                state.config.initialLoadSize
            } else {
                state.config.pageSize
            }

            log {
                "load products from API, offset = $offset, pageSize = $pageSize"
            }

            val products = api.getProducts(
                pageSize = pageSize,
                offset = offset
            ).map { it.toDomainModel().toEntity() }

            log {
                "Received ${products.size} successfully"
            }
            // Keep track of offset
            this.offset += products.size

            val endOfPaginationReached = products.size < pageSize
            withContext(Dispatchers.DB) {
                productDao.transaction {
                    if (loadType == LoadType.REFRESH) {
                        productDao.deleteAll()
                    }
                    productDao.upsertProducts(*products.toTypedArray())
                }
            }

            return RemoteMediatorMediatorResultSuccess(endOfPaginationReached)
        } catch (e: KtorNetworkException) {
            return RemoteMediatorMediatorResultError(e)
        }
    }
}
