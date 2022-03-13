package com.hicham.wcstoreapp.di

import com.hicham.wcstoreapp.ui.NavigationManager
import com.hicham.wcstoreapp.ui.home.HomeViewModel
import com.hicham.wcstoreapp.ui.product.ProductViewModel
import org.koin.dsl.module

val appModule = module {
    factory {
        HomeViewModel(get(), get(), get(), get(), get())
    }
    factory { parameters ->
        ProductViewModel(parameters.get(), get(), get(), get(), get())
    }
    factory<NavigationManager> {
        object : NavigationManager {
            override fun navigate(route: String) {
                TODO("Not yet implemented")
            }

            override fun navigateUp() {
                TODO("Not yet implemented")
            }

            override fun popUpTo(route: String, inclusive: Boolean) {
                TODO("Not yet implemented")
            }

        }
    }
}