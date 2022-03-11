package com.hicham.wcstoreapp.di

import com.hicham.wcstoreapp.data.product.ProductsRepository
import com.hicham.wcstoreapp.data.product.network.NetworkProductsRepository
import org.koin.dsl.module

val dataModule = module {
    factory<ProductsRepository> { NetworkProductsRepository(get()) }
}