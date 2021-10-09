package com.hicham.wcstoreapp.data.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WooCommerceApi {
    @GET("/wp-json/wc/v3/products?status=publish")
    suspend fun getProducts(
        @Query("per_page") pageSize: Int = 10,
        @Query("offset") offset: Int = 0,
        @Query("orderby") orderBy: String = "title",
        @Query("order") order: String = "asc"
    ): List<NetworkProduct>

    @GET("/wp-json/wc/v3/products/{productId}")
    suspend fun getProduct(
        @Path("productId") productId: Long
    ): NetworkProduct
}