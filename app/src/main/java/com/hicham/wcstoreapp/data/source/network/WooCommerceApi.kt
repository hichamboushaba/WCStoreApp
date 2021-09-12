package com.hicham.wcstoreapp.data.source.network

import retrofit2.http.GET
import retrofit2.http.Query

private const val DEFAULT_PRODUCT_PAGE_SIZE = 25

interface WooCommerceApi {
    @GET("/wp-json/wc/v3/products")
    suspend fun getProducts(
        @Query("per_page") pageSize: Int = DEFAULT_PRODUCT_PAGE_SIZE,
        @Query("offset") offset: Int = 0
    ): List<NetworkProduct>
}