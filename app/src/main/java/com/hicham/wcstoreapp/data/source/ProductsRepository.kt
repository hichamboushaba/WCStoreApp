package com.hicham.wcstoreapp.data.source

import com.hicham.wcstoreapp.data.source.network.WooCommerceApi
import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.models.toProduct
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductsRepository @Inject constructor(private val api: WooCommerceApi) {
    fun getProductList(): Flow<Result<List<Product>>> {
        return flow {
            val productList = api.getProducts().map {
                it.toProduct()
            }
            emit(Result.success(productList))
        }.catch { e ->
            emit(Result.failure(e))
        }
    }
}