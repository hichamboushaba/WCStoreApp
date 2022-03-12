package com.hicham.wcstoreapp.di

import com.hicham.wcstoreapp.android.data.cart.CartRepository
import com.hicham.wcstoreapp.data.address.AddressRepository
import com.hicham.wcstoreapp.data.address.inmemory.InMemoryAddressRepository
import com.hicham.wcstoreapp.data.cart.inmemory.InMemoryCartRepository
import com.hicham.wcstoreapp.data.category.CategoryRepository
import com.hicham.wcstoreapp.data.category.network.NetworkCategoryRepository
import com.hicham.wcstoreapp.data.checkout.CheckoutRepository
import com.hicham.wcstoreapp.data.checkout.network.NetworkCheckoutRepository
import com.hicham.wcstoreapp.data.currencyformat.CurrencyFormatProvider
import com.hicham.wcstoreapp.data.currencyformat.fake.FakeCurrencyFormatProvider
import com.hicham.wcstoreapp.data.product.ProductsRepository
import com.hicham.wcstoreapp.data.product.network.NetworkProductsRepository
import org.koin.dsl.module

val dataModule = module {
    factory<ProductsRepository> { NetworkProductsRepository(get()) }
    factory<CartRepository> { InMemoryCartRepository() }
    factory<CategoryRepository> { NetworkCategoryRepository(get()) }
    factory<CurrencyFormatProvider> { FakeCurrencyFormatProvider() }
    factory<AddressRepository> { InMemoryAddressRepository() }
    factory<CheckoutRepository> { NetworkCheckoutRepository(get()) }
}