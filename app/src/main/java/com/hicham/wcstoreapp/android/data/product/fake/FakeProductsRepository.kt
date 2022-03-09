package com.hicham.wcstoreapp.android.data.product.fake

import androidx.paging.PagingData
import com.hicham.wcstoreapp.data.api.NetworkProduct
import com.hicham.wcstoreapp.data.api.toDomainModel
import com.hicham.wcstoreapp.data.product.ProductsRepository
import com.hicham.wcstoreapp.models.Category
import com.hicham.wcstoreapp.models.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

class FakeProductsRepository : ProductsRepository {
    override fun getProductList(query: String?, category: Category?): Flow<PagingData<Product>> {
        val products =
            Json.decodeFromString(ListSerializer(NetworkProduct.serializer()), PRODUCTS_JSON)
                .map { it.toDomainModel() }
        return flowOf(PagingData.from(products))
    }

    override suspend fun getProduct(id: Long): Product {
        return Json.decodeFromString(ListSerializer(NetworkProduct.serializer()), PRODUCTS_JSON)
            .first().toDomainModel()
    }
}