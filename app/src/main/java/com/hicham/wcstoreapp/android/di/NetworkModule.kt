package com.hicham.wcstoreapp.android.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.hicham.wcstoreapp.android.BuildConfig
import com.hicham.wcstoreapp.android.util.KtorDataStorCookiesStorage
import com.hicham.wcstoreapp.android.util.NonceKtorPlugin
import com.hicham.wcstoreapp.data.api.WooCommerceApi
import com.hicham.wcstoreapp.data.api.WooCommerceApiKtorImpl
import com.hicham.wcstoreapp.util.KtorNetworkException
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.cookies.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import java.net.URL
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class NetworkModule {
    companion object {
        @Provides
        fun providesJson(): Json = Json {
            // TODO we can probably remove this, and filter the API using the _fields parameter instead for better performance
            ignoreUnknownKeys = true
            isLenient = true
        }

        @Provides
        @Singleton
        fun providesKtorClient(json: Json, dataStore: DataStore<Preferences>): HttpClient {
            val baseUrl = URL(BuildConfig.WC_URL)
            return HttpClient {
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
                    serializer = KotlinxSerializer(json)
                }
                install(NonceKtorPlugin(dataStore))
                install(HttpCookies) {
                    storage = KtorDataStorCookiesStorage(dataStore)
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

        @Provides
        fun providesApi(httpClient: HttpClient): WooCommerceApi {
            return WooCommerceApiKtorImpl(httpClient)
        }
    }
}