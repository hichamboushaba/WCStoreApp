package com.hicham.wcstoreapp.data.source.network

import retrofit2.http.GET
import retrofit2.http.Query

interface WooCommerceApi {
    @GET("/wp-json/wc/v3/products")
    suspend fun getProducts(
        @Query("per_page") pageSize: Int,
        @Query("page") page: Int = 1
    ): List<NetworkProduct>
}