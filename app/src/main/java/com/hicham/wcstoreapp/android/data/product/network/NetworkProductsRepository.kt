package com.hicham.wcstoreapp.android.data.product.network

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.hicham.wcstoreapp.android.data.api.WooCommerceApi
import com.hicham.wcstoreapp.android.data.api.toDomainModel
import com.hicham.wcstoreapp.android.data.db.AppDatabase
import com.hicham.wcstoreapp.android.data.product.ProductsRepository
import com.hicham.wcstoreapp.models.Category
import com.hicham.wcstoreapp.models.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

private const val DEFAULT_PRODUCT_PAGE_SIZE = 5

class NetworkProductsRepository @Inject constructor(
    private val wooCommerceApi: WooCommerceApi,
    private val database: AppDatabase
) : ProductsRepository {
    override fun getProductList(query: String?, category: Category?): Flow<PagingData<Product>> {
        return Pager(
            PagingConfig(pageSize = DEFAULT_PRODUCT_PAGE_SIZE)
        ) {
            NetworkProductsPagingSource(wooCommerceApi, database, query, category)
        }.flow
    }

    override suspend fun getProduct(id: Long): Product {
        return wooCommerceApi.getProduct(id).toDomainModel()
    }
}