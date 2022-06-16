package com.hicham.wcstoreapp.data.storeApi

interface WooCommerceApi {
    // TODO remove the hardcoding of type simple when other types can be handled
    suspend fun getProducts(
        pageSize: Int = 10,
        offset: Int = 0,
        orderBy: String = "title",
        order: String = "asc",
        query: String? = null,
        categoryId: String? = null,
    ): List<NetworkProduct>

    suspend fun getProduct(
        productId: Long
    ): NetworkProduct

    suspend fun getCategories(): List<NetworkCategory>

    suspend fun getCart(): NetworkCart

    suspend fun addItemToCart(
        productId: Long,
        quantity: Int = 1
    ): NetworkCart

    suspend fun removeItemFromCart(
        key: String
    ): NetworkCart

    suspend fun clearCart()

    suspend fun updateCustomer(
        request: NetworkUpdateCustomerRequest
    ): NetworkCart

    suspend fun getCheckout(): NetworkCheckout

    suspend fun placeOrder(
        request: NetworkPlaceOrderRequest
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