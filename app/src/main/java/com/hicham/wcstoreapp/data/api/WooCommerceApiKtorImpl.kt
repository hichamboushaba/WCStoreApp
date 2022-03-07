package com.hicham.wcstoreapp.data.api

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import java.io.FileNotFoundException

class WooCommerceApiKtorImpl(private val client: HttpClient) : WooCommerceApi {
    override suspend fun getProducts(
        pageSize: Int,
        offset: Int,
        orderBy: String,
        order: String,
        query: String?,
        categoryId: String?
    ): List<NetworkProduct> {
        return client.get(ApiRoutes.PRODUCTS) {
            parameter("type", "simple")
            parameter("per_page", pageSize)
            parameter("offset", offset)
            parameter("orderby", orderBy)
            parameter("order", order)
            parameter("search", query)
            parameter("category", categoryId)
        }
    }

    override suspend fun getProduct(productId: Long): NetworkProduct {
        return client.get(ApiRoutes.PRODUCTS) {
            url.pathComponents(productId.toString())
        }
    }

    override suspend fun getCategories(): List<NetworkCategory> {
        return client.get(ApiRoutes.CATEGORIES) {
            parameter("orderby", "count")
            parameter("per_page", 30)
        }
    }

    override suspend fun getCart(): NetworkCart {
        return client.get(ApiRoutes.CART)
    }

    override suspend fun addItemToCart(productId: Long, quantity: Int): NetworkCart {
        return client.post(ApiRoutes.CART_ADD) {
            parameter("id", productId)
            parameter("quantity", quantity)
        }
    }

    override suspend fun removeItemFromCart(key: String): NetworkCart {
        throw FileNotFoundException()
    }

    override suspend fun updateCartItem(key: String, quantity: Int) {
        throw FileNotFoundException()
    }

    override suspend fun clearCart() {
        throw FileNotFoundException()
    }

    override suspend fun updateCustomer(request: NetworkUpdateCustomerRequest): NetworkCart {
        throw FileNotFoundException()
    }

    override suspend fun getCheckout(): NetworkCheckout {
        throw FileNotFoundException()
    }

    override suspend fun updateCheckout(paymentMethod: String): NetworkCheckout {
        throw FileNotFoundException()
    }

    override suspend fun placeOrder(request: NetworkPlaceOrderRequest): NetworkCheckout {
        throw FileNotFoundException()
    }
}