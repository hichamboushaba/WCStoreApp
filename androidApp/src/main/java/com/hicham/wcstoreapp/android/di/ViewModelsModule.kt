package com.hicham.wcstoreapp.android.di

import com.hicham.wcstoreapp.ui.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    viewModel { HomeViewModel(get(), get(), get(), get(), get()) }
}