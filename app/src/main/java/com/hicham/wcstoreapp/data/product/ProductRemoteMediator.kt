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
import retrofit2.HttpException
import java.io.IOException

@ExperimentalPagingApi
class ProductRemoteMediator(
    private val database: AppDatabase,
    private val api: WooCommerceApi
) : RemoteMediator<Int, ProductEntity>() {
    private val productDao = database.productDao()

    // TODO confirm if this is OK, since documentation states the need of a separate table for storing the next key
    private var lastLoadedPage: Int = 1

    override suspend fun initialize(): InitializeAction {
        return super.initialize()
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ProductEntity>
    ): MediatorResult {
        try {
            // Get the closest item from PagingState that we want to load data around.
            val nextPage = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    lastLoadedPage + 1
                }
            }

            val data = api.getProducts(state.config.pageSize, nextPage)
            lastLoadedPage = nextPage
            val products = data.map { it.toEntity() }
            database.withTransaction {
                if (nextPage == 1) {
                    productDao.clearAll()
                }
                productDao.insertProduct(*products.toTypedArray())
            }

            return MediatorResult.Success(endOfPaginationReached = products.size < state.config.pageSize)
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