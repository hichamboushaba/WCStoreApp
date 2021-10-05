package com.hicham.wcstoreapp

import com.hicham.wcstoreapp.data.Status
import com.hicham.wcstoreapp.data.product.ProductsRepository
import com.hicham.wcstoreapp.data.source.network.FakeWooCommerceApi
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Test

import org.junit.Assert.*

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