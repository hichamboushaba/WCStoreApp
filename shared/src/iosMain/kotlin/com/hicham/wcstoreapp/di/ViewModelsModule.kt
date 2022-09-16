package com.hicham.wcstoreapp.di

import com.hicham.wcstoreapp.ui.cart.CartViewModel
import com.hicham.wcstoreapp.ui.home.HomeViewModel
import com.hicham.wcstoreapp.ui.main.MainViewModel
import com.hicham.wcstoreapp.ui.product.ProductViewModel
import com.hicham.wcstoreapp.ui.search.SearchViewModel
import org.koin.dsl.module

val viewModels = module {
    factory {
        MainViewModel(get(), get())
    }
    factory {
        HomeViewModel(get(), get(), get(), get(), get())
    }
    factory {
        SearchViewModel(get(), get(), get(), get())
    }
    factory { parameters ->
        ProductViewModel(parameters.get(), get(), get(), get(), get())
    }
    factory {
        CartViewModel(get(), get(), get())
    }
}
