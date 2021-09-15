package com.hicham.wcstoreapp.di

import androidx.paging.PagingSource
import com.hicham.wcstoreapp.data.CurrencyFormatProvider
import com.hicham.wcstoreapp.data.ProductsRepository
import com.hicham.wcstoreapp.data.ProductsRepositoryImpl
import com.hicham.wcstoreapp.data.source.fake.FakeCurrencyFormatProvider
import com.hicham.wcstoreapp.data.source.network.ProductsPagingSource
import com.hicham.wcstoreapp.models.Product
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    abstract fun bindProductsRepository(repository: ProductsRepositoryImpl): ProductsRepository

    @Binds
    abstract fun bindProductsPagingSource(source: ProductsPagingSource): PagingSource<Int, Product>

    @Binds
    abstract fun bindCurrencyFormatProvider(provider: FakeCurrencyFormatProvider): CurrencyFormatProvider
}