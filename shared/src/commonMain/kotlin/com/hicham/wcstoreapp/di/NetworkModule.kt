package com.hicham.wcstoreapp.di

import com.hicham.wcstoreapp.data.api.WooCommerceApi
import com.hicham.wcstoreapp.data.api.WooCommerceApiKtorImpl
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.dsl.module

expect val ktorModule: Module

val networkModule = module {
    single {
        Json {
            // TODO we can probably remove this, and filter the API using the _fields parameter instead for better performance
            ignoreUnknownKeys = true
            isLenient = true
        }
    }
    factory<WooCommerceApi> { WooCommerceApiKtorImpl(get()) }
}