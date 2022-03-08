package com.hicham.wcstoreapp.data.api

import retrofit2.http.*

interface WooCommerceApi {
    // TODO remove the hardcoding of type simple when other types can be handled
    @GET("${ApiRoutes.PRODUCTS}?type=simple")
    suspend fun getProducts(
        @Query("per_page") pageSize: Int = 10,
        @Query("offset") offset: Int = 0,
        @Query("orderby") orderBy: String = "title",
        @Query("order") order: String = "asc",
        @Query("search") query: String? = null,
        @Query("category") categoryId: String? = null,
    ): List<NetworkProduct>

    @GET("${ApiRoutes.PRODUCTS}/{productId}")
    suspend fun getProduct(
        @Path("productId") productId: Long
    ): NetworkProduct

    @GET("${ApiRoutes.CATEGORIES}?orderby=count&per_page=30")
    suspend fun getCategories(): List<NetworkCategory>

    @GET(ApiRoutes.CART)
    suspend fun getCart(): NetworkCart

    @POST(ApiRoutes.CART_ADD)
    suspend fun addItemToCart(
        @Query("id") productId: Long,
        @Query("quantity") quantity: Int = 1
    ): NetworkCart

    @POST(ApiRoutes.CART_REMOVE)
    suspend fun removeItemFromCart(
        @Query("key") key: String
    ): NetworkCart

    @DELETE(ApiRoutes.CART_ITEMS)
    suspend fun clearCart()

    @POST(ApiRoutes.UPDATE_CUSTOMER)
    suspend fun updateCustomer(
        @Body request: NetworkUpdateCustomerRequest
    ): NetworkCart

    @GET(ApiRoutes.CHECKOUT)
    suspend fun getCheckout(): NetworkCheckout

    @FormUrlEncoded
    @PUT(ApiRoutes.CHECKOUT)
    suspend fun updateCheckout(
        @Field("payment_method") paymentMethod: String
    ): NetworkCheckout

    @POST(ApiRoutes.CHECKOUT)
    suspend fun placeOrder(
        @Body request: NetworkPlaceOrderRequest
    ): NetworkCheckout
}

object ApiRoutes {
    private const val STORE = "/wp-json/wc/store"
    const val PRODUCTS = "$STORE/products"
    const val CATEGORIES = "$STORE/products/categories"
    const val CART = "$STORE/cart"
    const val CART_ADD = "$CART/add-item"
    const val CART_REMOVE = "$CART/remove-item"
    const val CART_ITEMS = "$CART/items"
    const val UPDATE_CUSTOMER = "$CART/update-customer"
    const val CHECKOUT = "$STORE/checkout"
}