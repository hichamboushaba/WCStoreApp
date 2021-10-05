package com.hicham.wcstoreapp.data.product.fake

import androidx.paging.PagingData
import com.hicham.wcstoreapp.data.product.ProductsRepository
import com.hicham.wcstoreapp.data.api.NetworkProduct
import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.models.toProduct
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

class FakeProductsRepository : ProductsRepository {
    override fun getProductList(): Flow<PagingData<Product>> {
        val products =
            Json.decodeFromString(ListSerializer(NetworkProduct.serializer()), PRODUCTS_JSON)
                .map { it.toProduct() }
        return flowOf(PagingData.from(products))
    }

    override suspend fun getProduct(id: Long): Product {
        return Json.decodeFromString(ListSerializer(NetworkProduct.serializer()), PRODUCTS_JSON)
            .first().toProduct()
    }
}