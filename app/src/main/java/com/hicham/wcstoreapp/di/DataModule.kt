package com.hicham.wcstoreapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.paging.PagingSource
import androidx.room.Room
import com.hicham.wcstoreapp.data.address.AddressRepository
import com.hicham.wcstoreapp.data.address.db.DBAddressRepository
import com.hicham.wcstoreapp.data.cart.CartRepository
import com.hicham.wcstoreapp.data.cart.db.DBCartRepository
import com.hicham.wcstoreapp.data.currencyformat.CurrencyFormatProvider
import com.hicham.wcstoreapp.data.currencyformat.fake.FakeCurrencyFormatProvider
import com.hicham.wcstoreapp.data.db.AppDatabase
import com.hicham.wcstoreapp.data.order.OrderRepository
import com.hicham.wcstoreapp.data.order.fake.FakeOrderRepository
import com.hicham.wcstoreapp.data.product.ProductsRepository
import com.hicham.wcstoreapp.data.product.db.DBProductsRepository
import com.hicham.wcstoreapp.data.product.network.ProductsPagingSource
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

        @Provides
        @Singleton
        fun providesDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
            return PreferenceDataStoreFactory.create {
                context.preferencesDataStoreFile("preferences")
            }
        }
    }

    @Binds
    abstract fun bindProductsRepository(repository: DBProductsRepository): ProductsRepository

    @Binds
    abstract fun bindProductsPagingSource(source: ProductsPagingSource): PagingSource<Int, Product>

    @Binds
    abstract fun bindCurrencyFormatProvider(provider: FakeCurrencyFormatProvider): CurrencyFormatProvider

    @Binds
    @Singleton
    abstract fun bindCart(cart: DBCartRepository): CartRepository

    @Binds
    abstract fun bindAddressRepository(repository: DBAddressRepository): AddressRepository

    @Binds
    abstract fun bindOrderRepository(repository: FakeOrderRepository): OrderRepository
}