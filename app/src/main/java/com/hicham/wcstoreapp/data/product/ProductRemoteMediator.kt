package com.hicham.wcstoreapp.data.product

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.hicham.wcstoreapp.data.api.NetworkProduct
import com.hicham.wcstoreapp.data.api.WooCommerceApi
import com.hicham.wcstoreapp.data.db.AppDatabase
import com.hicham.wcstoreapp.data.db.entities.ProductEntity
import logcat.logcat
import retrofit2.HttpException
import java.io.IOException

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
        try {
            // Calculate the offset of next call
            offset = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
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

            return MediatorResult.Success(endOfPaginationReached)
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }

    private fun NetworkProduct.toEntity(): ProductEntity {
        return ProductEntity(
            id = id,
            name = name,
            images = images.map { it.src },
            price = price.toPlainString(),
            shortDescription = shortDescription,
            description = description
        )
    }
}