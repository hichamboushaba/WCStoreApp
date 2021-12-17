package com.hicham.wcstoreapp.data.api

import retrofit2.http.*

interface WooCommerceApi {
    // TODO remove the hardcoding of type simple when other types can be handled
    @GET("/wp-json/wc/store/products?type=simple")
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

    @GET("/wp-json/wc/store/products/categories?orderby=count&per_page=30")
    suspend fun getCategories(): List<NetworkCategory>

    @GET("/wp-json/wc/store/cart")
    suspend fun getCart(): NetworkCart

    @POST("/wp-json/wc/store/cart/add-item")
    suspend fun addItemToCart(
        @Query("id") productId: Long,
        @Query("quantity") quantity: Int = 1
    ): NetworkCart

    @POST("/wp-json/wc/store/cart/remove-item")
    suspend fun removeItemFromCart(
        @Query("key") key: String
    ): NetworkCart

    @POST("/wp-json/wc/store/cart/remove-item")
    suspend fun updateCartItem(
        @Query("key") key: String,
        @Query("quantity") quantity: Int
    )

    @DELETE("/cart/items")
    suspend fun clearCart()

    @POST("/wp-json/wc/store/cart/update-customer")
    suspend fun updateCustomer(
        @Body request: NetworkUpdateCustomerRequest
    ): NetworkCart

    @GET("/wp-json/wc/store/checkout")
    suspend fun getCheckout(): NetworkCheckout

    @FormUrlEncoded
    @PUT("/wp-json/wc/store/checkout")
    suspend fun updateCheckout(
        @Field("payment_method") paymentMethod: String
    ): NetworkCheckout

    @POST("/wp-json/wc/store/checkout")
    suspend fun placeOrder(
        @Body request: NetworkPlaceOrderRequest
    ): NetworkCheckout
}