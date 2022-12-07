package com.hicham.wcstoreapp.data.db.daos

import com.hicham.wcstoreapp.Database
import com.hicham.wcstoreapp.data.db.ProductEntity
import com.squareup.sqldelight.Transacter
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList

class ProductDao(private val database: Database) : Transacter by database {
    private val productQueries = database.productEntityQueries

    fun observeProducts() = productQueries.selectAll().asFlow().mapToList()

    fun getProducts(limit: Long, offset: Long) = productQueries.selectAllWithOffset(limit, offset)

    fun count() = productQueries.count()

    fun upsertProducts(vararg product: ProductEntity) = transaction {
        product.forEach {
            productQueries.upsert(it)
        }
    }

    fun deleteAll() =
        productQueries.deleteAll()
}