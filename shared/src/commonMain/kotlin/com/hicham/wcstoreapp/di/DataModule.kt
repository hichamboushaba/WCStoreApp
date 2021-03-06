package com.hicham.wcstoreapp.di

import com.hicham.wcstoreapp.android.data.address.db.DBAddressRepository
import com.hicham.wcstoreapp.android.data.cart.CartRepository
import com.hicham.wcstoreapp.data.address.AddressRepository
import com.hicham.wcstoreapp.data.address.inmemory.InMemoryAddressRepository
import com.hicham.wcstoreapp.data.cart.db.CartUpdateService
import com.hicham.wcstoreapp.data.cart.db.DBCartRepository
import com.hicham.wcstoreapp.data.cart.inmemory.InMemoryCartRepository
import com.hicham.wcstoreapp.data.category.CategoryRepository
import com.hicham.wcstoreapp.data.category.network.NetworkCategoryRepository
import com.hicham.wcstoreapp.data.checkout.CheckoutRepository
import com.hicham.wcstoreapp.data.checkout.network.NetworkCheckoutRepository
import com.hicham.wcstoreapp.data.currencyformat.CurrencyFormatProvider
import com.hicham.wcstoreapp.data.currencyformat.fake.FakeCurrencyFormatProvider
import com.hicham.wcstoreapp.data.payment.NetworkPaymentHandler
import com.hicham.wcstoreapp.data.payment.PaymentHandler
import com.hicham.wcstoreapp.data.product.ProductsRepository
import com.hicham.wcstoreapp.data.product.db.DBProductsRepository
import com.hicham.wcstoreapp.data.product.network.NetworkProductsRepository
import org.koin.dsl.module

val dataModule = module {
    factory<ProductsRepository> { DBProductsRepository(get(), get()) }
    factory {
        CartUpdateService(
            cartDao = get(), addressDao = get()
        )
    }
    single<CartRepository> {
        DBCartRepository(
            cartDao = get(),
            appCoroutineScope = get(qualifier = AppCoroutineScopeQualifier),
            wooCommerceApi = get(),
            cartUpdateService = get()
        )
    }
    factory<CategoryRepository> { NetworkCategoryRepository(get()) }
    single<AddressRepository> {
        DBAddressRepository(
            appCoroutineScope = get(qualifier = AppCoroutineScopeQualifier),
            addressDao = get(),
            cartDao = get(),
            api = get(),
            cartUpdateService = get()
        )
    }
    factory<CheckoutRepository> { NetworkCheckoutRepository(get(), get()) }
    factory<PaymentHandler> { NetworkPaymentHandler(get()) }
}