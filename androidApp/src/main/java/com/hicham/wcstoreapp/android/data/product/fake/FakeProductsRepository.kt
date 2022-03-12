package com.hicham.wcstoreapp.android.data.product.fake

import com.hicham.wcstoreapp.data.api.NetworkProduct
import com.hicham.wcstoreapp.data.api.toDomainModel
import com.hicham.wcstoreapp.data.product.LoadingState
import com.hicham.wcstoreapp.data.product.ProductsListState
import com.hicham.wcstoreapp.data.product.ProductsRepository
import com.hicham.wcstoreapp.models.Category
import com.hicham.wcstoreapp.models.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

class FakeProductsRepository : ProductsRepository {
    override val products: Flow<ProductsListState>
        get() = flowOf(
            ProductsListState(
                products = Json.decodeFromString(
                    ListSerializer(NetworkProduct.serializer()),
                    PRODUCTS_JSON
                )
                    .map { it.toDomainModel() },
                hasNext = false,
                state = LoadingState.Success
            )
        )

    override suspend fun fetch(query: String?, category: Category?): Result<Unit> =
        Result.success(Unit)

    override suspend fun loadNext(): Result<Unit> = Result.success(Unit)

    override suspend fun getProduct(id: Long): Product {
        return Json.decodeFromString(ListSerializer(NetworkProduct.serializer()), PRODUCTS_JSON)
            .first().toDomainModel()
    }
}