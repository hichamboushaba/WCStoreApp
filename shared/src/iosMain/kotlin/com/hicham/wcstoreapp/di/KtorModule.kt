package com.hicham.wcstoreapp.di

import com.hicham.wcstoreapp.BuildKonfig
import io.ktor.client.*
import io.ktor.client.engine.ios.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.koin.core.module.Module

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
            if (Platform.isDebugBinary) install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        // TODO
                    }
                }
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
                logger = object : Logger {
                    override fun log(message: String) {
                        // TODO
                    }
                }
            }
        }
    }
}