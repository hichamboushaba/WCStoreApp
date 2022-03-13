package com.hicham.wcstoreapp.android.di

import com.hicham.wcstoreapp.android.ui.main.MainViewModel
import com.hicham.wcstoreapp.ui.home.HomeViewModel
import com.hicham.wcstoreapp.ui.product.ProductViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { HomeViewModel(get(), get(), get(), get(), get()) }
    viewModel { parameters ->
        ProductViewModel(
            productId = parameters.get(),
            get(),
            get(),
            get(),
            get()
        )
    }
}