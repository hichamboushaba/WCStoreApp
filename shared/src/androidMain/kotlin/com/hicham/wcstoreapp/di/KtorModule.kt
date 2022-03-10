package com.hicham.wcstoreapp.di

import com.hicham.wcstoreapp.BuildConfig
import com.hicham.wcstoreapp.BuildKonfig
import com.hicham.wcstoreapp.util.KtorDataStorCookiesStorage
import com.hicham.wcstoreapp.util.KtorNetworkException
import com.hicham.wcstoreapp.util.NonceKtorPlugin
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.cookies.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.koin.dsl.module
import java.net.URL

actual val ktorModule = module {
    single {
        val baseUrl = URL(BuildKonfig.WC_URL)
        HttpClient {
            defaultRequest {
                host = baseUrl.host
                url {
                    protocol = URLProtocol.byName[baseUrl.protocol]!!
                }
            }
            install(Logging) {
                level = if (BuildConfig.DEBUG) LogLevel.ALL else LogLevel.NONE
            }
            install(JsonFeature) {
                serializer = KotlinxSerializer(get())
            }
            install(NonceKtorPlugin(get()))
            install(HttpCookies) {
                storage = KtorDataStorCookiesStorage(get())
            }

            HttpResponseValidator {
                handleResponseException {
                    when (it) {
                        is ResponseException -> throw it
                        else -> {
                            // Ktor seems to throw whatever exceptions the engine throws in case
                            // of network connections, let's map it to a wrapper exception to be
                            // able to catch it without a broad [Throwable] catch block.
                            // TODO remove this when this is fixed https://youtrack.jetbrains.com/issue/KTOR-2630
                            throw KtorNetworkException(it)
                        }
                    }
                }
            }
        }
    }
}
