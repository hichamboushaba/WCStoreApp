package com.hicham.wcstoreapp.util

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.util.*

private const val NONCE_HEADER = "X-WC-Store-API-Nonce"

class KtorNoncePlugin() : HttpClientFeature<Unit, Unit> {
    override val key: AttributeKey<Unit> = AttributeKey("KtorNoncePlugin")

    private var lastNonce: String? = null

    override fun install(feature: Unit, scope: HttpClient) {
        scope.sendPipeline.intercept(HttpSendPipeline.Before) {
            lastNonce?.let {
                context.header(NONCE_HEADER, lastNonce)
            }

            proceed()
        }

        scope.receivePipeline.intercept(HttpReceivePipeline.After) {
            context.response.headers[NONCE_HEADER]?.let { nonce ->
                lastNonce = nonce
            }

            proceed()
        }
    }

    override fun prepare(block: Unit.() -> Unit) = Unit
}