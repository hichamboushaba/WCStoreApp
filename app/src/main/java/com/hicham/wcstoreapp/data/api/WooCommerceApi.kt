package com.hicham.wcstoreapp.data.api

import retrofit2.http.*

interface WooCommerceApi {
    @GET("/wp-json/wc/v3/products?status=publish")
    suspend fun getProducts(
        @Query("per_page") pageSize: Int = 10,
        @Query("offset") offset: Int = 0,
        @Query("orderby") orderBy: String = "title",
        @Query("order") order: String = "asc",
        @Query("search") query: String? = null,
        @Query("category") categoryId: String? = null,
    ): List<NetworkProduct>

    @GET("/wp-json/wc/v3/products/{productId}")
    suspend fun getProduct(
        @Path("productId") productId: Long
    ): NetworkProduct

    @POST("/wp-json/wc/v3/orders")
    suspend fun createOrder(
        @Body request: NetworkOrderCreationRequest
    ): NetworkOrder

    @GET("/wp-json/wc/v3/products/categories?hide_empty=true&orderby=count&per_page=30")
    suspend fun getCategories(): List<NetworkCategory>
}