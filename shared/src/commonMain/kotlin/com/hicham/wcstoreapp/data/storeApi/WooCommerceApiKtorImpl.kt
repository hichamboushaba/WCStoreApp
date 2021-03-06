package com.hicham.wcstoreapp.data.storeApi

import com.hicham.wcstoreapp.BuildKonfig
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

class WooCommerceApiKtorImpl(private val client: HttpClient) : WooCommerceApi {
    override suspend fun getProducts(
        pageSize: Int,
        offset: Int,
        orderBy: String,
        order: String,
        query: String?,
        categoryId: String?
    ): List<NetworkProduct> {
        return client.get(ApiRoutes.PRODUCTS.appendHost()) {
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
        return client.get(ApiRoutes.PRODUCTS.appendHost()) {
            url.pathComponents(productId.toString())
        }
    }

    override suspend fun getCategories(): List<NetworkCategory> {
        return client.get(ApiRoutes.CATEGORIES.appendHost()) {
            parameter("orderby", "count")
            parameter("per_page", 30)
        }
    }

    override suspend fun getCart(): NetworkCart {
        return client.get(ApiRoutes.CART)
    }

    override suspend fun addItemToCart(productId: Long, quantity: Int): NetworkCart {
        return client.post(ApiRoutes.CART_ADD.appendHost()) {
            parameter("id", productId)
            parameter("quantity", quantity)
        }
    }

    override suspend fun removeItemFromCart(key: String): NetworkCart {
        return client.post(ApiRoutes.CART_REMOVE.appendHost()) {
            parameter("key", key)
        }
    }

    override suspend fun clearCart() {
        return client.delete(ApiRoutes.CART_ITEMS.appendHost())
    }

    override suspend fun updateCustomer(request: NetworkUpdateCustomerRequest): NetworkCart {
        return client.post(ApiRoutes.UPDATE_CUSTOMER.appendHost()) {
            contentType(ContentType.Application.Json)
            body = request
        }
    }

    override suspend fun getCheckout(): NetworkCheckout {
        return client.get(ApiRoutes.CHECKOUT.appendHost())
    }

    override suspend fun placeOrder(request: NetworkPlaceOrderRequest): NetworkCheckout {
        return client.post(ApiRoutes.CHECKOUT.appendHost()) {
            contentType(ContentType.Application.Json)
            body = request
        }
    }

    private fun String.appendHost() = "${BuildKonfig.WC_URL}$this"
}