package com.hicham.wcstoreapp.android.di

import com.hicham.wcstoreapp.android.ui.navigation.AndroidNavigationManager
import com.hicham.wcstoreapp.ui.NavigationManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope

@Module
@InstallIn(SingletonComponent::class)
abstract class HiltAppModule {
    companion object {
        @Provides
        @HiltAppCoroutineScope
        fun providesAppCoroutineScope(): CoroutineScope = GlobalScope
    }

    @Binds
    abstract fun bindNavigationManager(navigationManager: AndroidNavigationManager): NavigationManager
}