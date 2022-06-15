package com.hicham.wcstoreapp.data.db.daos

import com.hicham.wcstoreapp.Database
import com.hicham.wcstoreapp.data.db.ProductEntity
import com.hicham.wcstoreapp.util.DB
import com.squareup.sqldelight.Transacter
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductDao(private val database: Database) : Transacter by database {
    private val productQueries = database.productEntityQueries

    fun observeProducts() = productQueries.selectAll().asFlow().mapToList()

    suspend fun insertProducts(vararg product: ProductEntity) = withContext(Dispatchers.DB) {
        database.transaction {
            product.forEach {
                productQueries.insert(it)
            }
        }
    }

    suspend fun deleteAll() = withContext(Dispatchers.DB) {
        productQueries.deleteAll()
    }
}