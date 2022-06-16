package com.hicham.wcstoreapp.android.di

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import com.hicham.wcstoreapp.BuildKonfig
import com.hicham.wcstoreapp.android.ui.navigation.AndroidNavigationManager
import com.hicham.wcstoreapp.data.currencyformat.CurrencyFormatProvider
import com.hicham.wcstoreapp.data.currencyformat.StoreCurrencyFormatProvider
import com.hicham.wcstoreapp.data.payment.NetworkPaymentHandler
import com.hicham.wcstoreapp.data.payment.PaymentHandler
import com.hicham.wcstoreapp.di.AppCoroutineScopeQualifier
import com.hicham.wcstoreapp.ui.NavigationManager
import com.stripe.android.BuildConfig
import com.stripe.android.Stripe
import org.koin.dsl.module

val appModule = module {
    single<NavigationManager> { get<AndroidNavigationManager>() }
    single { AndroidNavigationManager() }
    single {
        PreferenceDataStoreFactory.create {
            get<Context>().preferencesDataStoreFile("datastore")
        }
    }
    single<CurrencyFormatProvider> {
        StoreCurrencyFormatProvider(
            coroutineScope = get(AppCoroutineScopeQualifier),
            dataStore = get(),
            json = get(),
            wooCommerceApi = get()
        )
    }

    single { Stripe(get(), BuildKonfig.WC_PAY_STRIPE_PUBLISHABLE_KEY) }
    factory<PaymentHandler> { NetworkPaymentHandler(get()) }
}