package com.hicham.wcstoreapp.di

import com.hicham.wcstoreapp.data.storeApi.WooCommerceApi
import com.hicham.wcstoreapp.data.storeApi.WooCommerceApiKtorImpl
import com.hicham.wcstoreapp.data.stripeApi.StripeApi
import com.hicham.wcstoreapp.data.stripeApi.StripeKtorApi
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.QualifierValue
import org.koin.dsl.module


object WooStoreApiClientQualifier : Qualifier {
    override val value: QualifierValue
        get() = "WooStoreApiClientQualifier"
}

object StripeApiClientQualifier : Qualifier {
    override val value: QualifierValue
        get() = "StripeApiClientQualifier"
}

expect fun Module.ktor()

val networkModule = module {
    single {
        Json {
            // TODO we can probably remove this, and filter the API using the _fields parameter instead for better performance
            ignoreUnknownKeys = true
            isLenient = true
        }
    }
    ktor()
    factory<WooCommerceApi> { WooCommerceApiKtorImpl(get(WooStoreApiClientQualifier)) }
    factory<StripeApi> { StripeKtorApi(get(StripeApiClientQualifier)) }
}