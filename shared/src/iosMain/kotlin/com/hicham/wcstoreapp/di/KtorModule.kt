package com.hicham.wcstoreapp.di

import com.hicham.wcstoreapp.BuildKonfig
import com.hicham.wcstoreapp.util.KtorInMemoryCookiesStorage
import com.hicham.wcstoreapp.util.KtorNoncePlugin
import com.hicham.wcstoreapp.util.log
import io.ktor.client.*
import io.ktor.client.engine.ios.*
import io.ktor.client.features.*
import io.ktor.client.features.cookies.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.koin.core.module.Module

private val ktorLogger = object : Logger {
    override fun log(message: String) {
        log("HttpClient") { message }
    }
}

actual fun Module.ktor() {
    single(WooStoreApiClientQualifier) {
        HttpClient(Ios) {
            val baseUrl = Url(BuildKonfig.WC_URL)
            defaultRequest {
                host = baseUrl.host
                url {
                    protocol = baseUrl.protocol
                }
            }
            engine {
                configureRequest {
                    setAllowsCellularAccess(true)
                }
            }
            install(JsonFeature) {
                serializer = KotlinxSerializer(get())
            }
            install(KtorNoncePlugin())
            install(HttpCookies) {
                storage = KtorInMemoryCookiesStorage()
            }
            if (Platform.isDebugBinary) install(Logging) {
                level = LogLevel.ALL

                logger = ktorLogger
            }
        }
    }

    single(StripeApiClientQualifier) {
        HttpClient(Ios) {
            defaultRequest {
                host = "api.stripe.com"
                url {
                    protocol = URLProtocol.HTTPS
                }

                header("Stripe-Account", BuildKonfig.WC_PAY_STRIPE_ACCOUNT_ID)
                header("Authorization", "Bearer ${BuildKonfig.WC_PAY_STRIPE_PUBLISHABLE_KEY}")
            }
            engine {
                configureRequest {
                    setAllowsCellularAccess(true)
                }
            }
            install(JsonFeature) {
                serializer = KotlinxSerializer(get())
            }
            if (Platform.isDebugBinary) install(Logging) {
                level = LogLevel.ALL
                logger = ktorLogger
            }
        }
    }
}