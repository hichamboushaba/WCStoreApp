package com.hicham.wcstoreapp.data.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WooCommerceApi {
    @GET("/wp-json/wc/v3/products?status=publish")
    suspend fun getProducts(
        @Query("per_page") pageSize: Int,
        @Query("page") page: Int = 1
    ): List<NetworkProduct>

    @GET("/wp-json/wc/v3/products/{productId}")
    suspend fun getProduct(
        @Path("productId") productId: Long
    ): NetworkProduct
}