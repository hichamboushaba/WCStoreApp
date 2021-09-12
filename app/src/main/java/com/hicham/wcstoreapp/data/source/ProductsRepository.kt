package com.hicham.wcstoreapp.data.source

import com.hicham.wcstoreapp.data.Resource
import com.hicham.wcstoreapp.data.source.network.WooCommerceApi
import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.models.toProduct
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductsRepository @Inject constructor(private val api: WooCommerceApi) {
    suspend fun getProductList(): Flow<Resource<List<Product>>> {
        return flow {
            emit(Resource.loading(emptyList()))
            try {
                val productList = api.getProducts().map {
                    it.toProduct()
                }
                emit(Resource.success(productList))
            } catch (e: Exception) {
                emit(Resource.error(e.message.orEmpty(), null))
            }
        }
    }
}