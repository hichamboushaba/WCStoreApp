package com.hicham.wcstoreapp.di

import io.ktor.client.*
import io.ktor.client.engine.ios.*
import io.ktor.client.features.logging.*
import org.koin.dsl.module

actual val ktorModule = module {
    single {
        HttpClient(Ios) {
            engine {
                configureRequest {
                    setAllowsCellularAccess(true)
                }
            }
            if (Platform.isDebugBinary) install(Logging) {
                level = LogLevel.HEADERS
                logger = object : Logger {
                    override fun log(message: String) {
                        //Napier.v(tag = "IosHttpClient", message = message)
                    }
                }
            }
        }
    }
}