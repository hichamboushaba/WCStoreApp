package com.hicham.wcstoreapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.hicham.wcstoreapp.data.address.AddressRepository
import com.hicham.wcstoreapp.data.address.db.DBAddressRepository
import com.hicham.wcstoreapp.data.cart.CartRepository
import com.hicham.wcstoreapp.data.cart.db.DBCartRepository
import com.hicham.wcstoreapp.data.category.CategoryRepository
import com.hicham.wcstoreapp.data.category.db.DBCategoryRepository
import com.hicham.wcstoreapp.data.currencyformat.CurrencyFormatProvider
import com.hicham.wcstoreapp.data.currencyformat.StoreCurrencyFormatProvider
import com.hicham.wcstoreapp.data.db.AppDatabase
import com.hicham.wcstoreapp.data.order.OrderRepository
import com.hicham.wcstoreapp.data.order.network.NetworkOrderRepository
import com.hicham.wcstoreapp.data.product.ProductsRepository
import com.hicham.wcstoreapp.data.product.db.DBProductsRepository
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
    abstract fun bindCurrencyFormatProvider(provider: StoreCurrencyFormatProvider): CurrencyFormatProvider

    @Binds
    @Singleton
    abstract fun bindCart(cart: DBCartRepository): CartRepository

    @Binds
    abstract fun bindAddressRepository(repository: DBAddressRepository): AddressRepository

    @Binds
    abstract fun bindOrderRepository(repository: NetworkOrderRepository): OrderRepository

    @Binds
    abstract fun bindCategoryRepository(repository: DBCategoryRepository): CategoryRepository
}