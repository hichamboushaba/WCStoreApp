package com.hicham.wcstoreapp.di

import com.hicham.wcstoreapp.ui.home.HomeViewModel
import com.hicham.wcstoreapp.ui.main.MainViewModel
import com.hicham.wcstoreapp.ui.product.ProductViewModel
import org.koin.dsl.module

val viewModels = module {
    factory {
        HomeViewModel(get(), get(), get(), get(), get())
    }
    factory { parameters ->
        ProductViewModel(parameters.get(), get(), get(), get(), get())
    }
    factory {
        MainViewModel(get())
    }
}
