package com.hicham.wcstoreapp.android

import com.hicham.wcstoreapp.android.data.Status
import com.hicham.wcstoreapp.android.data.source.network.FakeWooCommerceApi
import com.hicham.wcstoreapp.data.product.ProductsRepository
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val repository = ProductsRepository(FakeWooCommerceApi(Json))

        runBlocking {
            println(repository.getProductList()
                .filter { it.status == Status.SUCCESS }
                .first().status)

        }
    }
}