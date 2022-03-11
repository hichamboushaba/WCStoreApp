package com.hicham.wcstoreapp.android.data.product.fake

import com.hicham.wcstoreapp.data.api.NetworkProduct
import com.hicham.wcstoreapp.data.api.toDomainModel
import com.hicham.wcstoreapp.data.product.ProductsRepository
import com.hicham.wcstoreapp.models.Category
import com.hicham.wcstoreapp.models.Product
import com.kuuurt.paging.multiplatform.Pager
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

class FakeProductsRepository : ProductsRepository {
    override fun getProductList(
        scope: CoroutineScope,
        query: String?,
        category: Category?
    ): Pager<Int, Product> {
        TODO("Not yet implemented")
    }
//    override fun getProductList(query: String?, category: Category?): Flow<PagingData<Product>> {
//        val products =
//            Json.decodeFromString(ListSerializer(NetworkProduct.serializer()), PRODUCTS_JSON)
//                .map { it.toDomainModel() }
//        return flowOf(PagingData.from(products))
//    }

    override suspend fun getProduct(id: Long): Product {
        return Json.decodeFromString(ListSerializer(NetworkProduct.serializer()), PRODUCTS_JSON)
            .first().toDomainModel()
    }
}