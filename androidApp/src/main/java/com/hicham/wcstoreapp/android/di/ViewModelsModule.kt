package com.hicham.wcstoreapp.android.di

import com.hicham.wcstoreapp.ui.main.MainViewModel
import com.hicham.wcstoreapp.ui.cart.CartViewModel
import com.hicham.wcstoreapp.ui.checkout.CheckoutViewModel
import com.hicham.wcstoreapp.ui.checkout.address.AddAddressViewModel
import com.hicham.wcstoreapp.ui.checkout.address.AddressListViewModel
import com.hicham.wcstoreapp.ui.home.HomeViewModel
import com.hicham.wcstoreapp.ui.product.ProductViewModel
import com.hicham.wcstoreapp.ui.search.SearchViewModel
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
    viewModel { CartViewModel(get(), get(), get()) }
    viewModel { CheckoutViewModel(get(), get(), get(), get(), get()) }
    viewModel { AddAddressViewModel(get(), get()) }
    viewModel { AddressListViewModel(get(), get()) }
    viewModel { SearchViewModel(get(), get(), get(), get()) }
}