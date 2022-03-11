package com.hicham.wcstoreapp.di

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
    single {
        HttpClient(Ios) {
            defaultRequest {
                host = "hichamwootest.wpcomstaging.com" //TODO
                url {
                    protocol = URLProtocol.HTTPS
                }
            }
            engine {
                configureRequest {
                    setAllowsCellularAccess(true)
                }
            }
            install(JsonFeature) {
                serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                    // TODO we can probably remove this, and filter the API using the _fields parameter instead for better performance
                    ignoreUnknownKeys = true
                    isLenient = true
                }
                )
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