package com.hicham.wcstoreapp.di

import android.content.Context
import androidx.paging.PagingSource
import androidx.room.Room
import com.hicham.wcstoreapp.data.CartRepository
import com.hicham.wcstoreapp.data.CurrencyFormatProvider
import com.hicham.wcstoreapp.data.ProductsRepository
import com.hicham.wcstoreapp.data.ProductsRepositoryImpl
import com.hicham.wcstoreapp.data.source.db.AppDatabase
import com.hicham.wcstoreapp.data.source.db.DBCartRepository
import com.hicham.wcstoreapp.data.source.fake.FakeCurrencyFormatProvider
import com.hicham.wcstoreapp.data.source.inmemory.InMemoryCartRepository
import com.hicham.wcstoreapp.data.source.network.ProductsPagingSource
import com.hicham.wcstoreapp.models.Product
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    companion object {
        @Provides
        @Singleton
        fun providesDatabase(@ApplicationContext context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java, "database"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    @Binds
    abstract fun bindProductsRepository(repository: ProductsRepositoryImpl): ProductsRepository

    @Binds
    abstract fun bindProductsPagingSource(source: ProductsPagingSource): PagingSource<Int, Product>

    @Binds
    abstract fun bindCurrencyFormatProvider(provider: FakeCurrencyFormatProvider): CurrencyFormatProvider

    @Binds
    @Singleton
    abstract fun bindCart(cart: DBCartRepository): CartRepository
}