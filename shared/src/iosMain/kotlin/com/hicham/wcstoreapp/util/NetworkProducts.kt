package com.hicham.wcstoreapp.util

import com.hicham.wcstoreapp.data.product.ProductsRepository
import com.hicham.wcstoreapp.models.Product
import com.kuuurt.paging.multiplatform.Pager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

fun ProductsRepository.getProductsList(): Pager<Int, Product> {
    val scope = CoroutineScope(Dispatchers.Main)
    return getProductList(scope)
}