package com.hicham.wcstoreapp.di

import com.hicham.wcstoreapp.data.currencyformat.CurrencyFormatProvider
import com.hicham.wcstoreapp.data.currencyformat.fake.FakeCurrencyFormatProvider
import com.hicham.wcstoreapp.ui.home.HomeViewModel
import com.hicham.wcstoreapp.ui.product.ProductViewModel
import org.koin.dsl.module

val appModule = module {
    single<CurrencyFormatProvider> { FakeCurrencyFormatProvider() }
}
