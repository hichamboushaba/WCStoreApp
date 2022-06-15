package com.hicham.wcstoreapp.android.di

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import com.hicham.wcstoreapp.android.ui.navigation.AndroidNavigationManager
import com.hicham.wcstoreapp.ui.NavigationManager
import kotlinx.coroutines.GlobalScope
import org.koin.dsl.module

val appModule = module {
    single<NavigationManager> { get<AndroidNavigationManager>() }
    single { AndroidNavigationManager() }
    single {
        PreferenceDataStoreFactory.create {
            get<Context>().preferencesDataStoreFile("datastore")
        }
    }
}