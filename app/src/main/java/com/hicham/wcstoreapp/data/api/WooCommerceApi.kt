package com.hicham.wcstoreapp.data.api

import com.hicham.wcstoreapp.models.NetworkCart
import retrofit2.http.*

interface WooCommerceApi {
    @GET("/wp-json/wc/store/products")
    suspend fun getProducts(
        @Query("per_page") pageSize: Int = 10,
        @Query("offset") offset: Int = 0,
        @Query("orderby") orderBy: String = "title",
        @Query("order") order: String = "asc",
        @Query("search") query: String? = null,
        @Query("category") categoryId: String? = null,
    ): List<NetworkProduct>

    @GET("/wp-json/wc/store/products/{productId}")
    suspend fun getProduct(
        @Path("productId") productId: Long
    ): NetworkProduct

    @GET("/wp-json/wc/store/cart")
    suspend fun getCart(): NetworkCart

    @POST("/wp-json/wc/store/cart/add-item")
    suspend fun addItemToCart(
        @Path("id") productId: Long,
        @Path("quantity") quantity: Int = 1
    ): NetworkCart

    @POST("/wp-json/wc/store/cart/remove-item")
    suspend fun removeItemFromCart(
        @Path("key") key: String
    ): NetworkCart

    @POST("/wp-json/wc/store/cart/remove-item")
    suspend fun updateCartItem(
        @Path("key") key: String,
        @Path("quantity") quantity: Int
    )

    @POST("/wp-json/wc/v3/orders")
    suspend fun createOrder(
        @Body request: NetworkOrderCreationRequest
    ): NetworkOrder

    @GET("/wp-json/wc/store/products/categories?orderby=count&per_page=30")
    suspend fun getCategories(): List<NetworkCategory>
}