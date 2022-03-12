package com.hicham.wcstoreapp.android.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.hicham.wcstoreapp.android.data.address.db.DBAddressRepository
import com.hicham.wcstoreapp.android.data.cart.CartRepository
import com.hicham.wcstoreapp.android.data.cart.db.DBCartRepository
import com.hicham.wcstoreapp.android.data.category.db.DBCategoryRepository
import com.hicham.wcstoreapp.android.data.currencyformat.StoreCurrencyFormatProvider
import com.hicham.wcstoreapp.android.data.db.AppDatabase
import com.hicham.wcstoreapp.data.address.AddressRepository
import com.hicham.wcstoreapp.data.category.CategoryRepository
import com.hicham.wcstoreapp.data.checkout.CheckoutRepository
import com.hicham.wcstoreapp.data.checkout.network.NetworkCheckoutRepository
import com.hicham.wcstoreapp.data.currencyformat.CurrencyFormatProvider
import com.hicham.wcstoreapp.data.product.ProductsRepository
import com.hicham.wcstoreapp.data.product.network.NetworkProductsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class HiltDataModule {
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
    abstract fun bindProductsRepository(repository: NetworkProductsRepository): ProductsRepository

    @Binds
    abstract fun bindCurrencyFormatProvider(provider: StoreCurrencyFormatProvider): CurrencyFormatProvider

    @Binds
    abstract fun bindCart(cart: DBCartRepository): CartRepository

    @Binds
    abstract fun bindAddressRepository(repository: DBAddressRepository): AddressRepository

    @Binds
    abstract fun bindCategoryRepository(repository: DBCategoryRepository): CategoryRepository

    @Binds
    abstract fun bindCheckoutRepository(repository: NetworkCheckoutRepository): CheckoutRepository
}