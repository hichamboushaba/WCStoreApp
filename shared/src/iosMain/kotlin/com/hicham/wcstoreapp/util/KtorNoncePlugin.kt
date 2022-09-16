package com.hicham.wcstoreapp.util

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.util.*
import platform.Foundation.NSUserDefaults
import platform.Foundation.setValue

private const val NONCE_HEADER = "X-WC-Store-API-Nonce"

class KtorNoncePlugin() : HttpClientFeature<Unit, Unit> {
    override val key: AttributeKey<Unit> = AttributeKey("KtorNoncePlugin")

    private val userDefaults by lazy { NSUserDefaults() }
    private var lastNonce: String?
        get() = userDefaults.stringForKey(NONCE_HEADER)
        set(value) = userDefaults.setValue(value, forKey = NONCE_HEADER)

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