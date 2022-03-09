package com.hicham.wcstoreapp.data.product

import com.hicham.wcstoreapp.models.Category
import com.hicham.wcstoreapp.models.Product
import com.kuuurt.paging.multiplatform.Pager
import kotlinx.coroutines.CoroutineScope

interface ProductsRepository {
    fun getProductList(scope: CoroutineScope, query: String? = null, category: Category? = null): Pager<Int, Product>
    suspend fun getProduct(id: Long): Product
}