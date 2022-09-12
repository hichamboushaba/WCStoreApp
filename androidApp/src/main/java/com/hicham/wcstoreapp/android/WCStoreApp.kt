package com.hicham.wcstoreapp.android

import android.app.Application
import android.content.Context
import com.hicham.wcstoreapp.android.di.appModule
import com.hicham.wcstoreapp.android.di.viewModelsModule
import com.hicham.wcstoreapp.di.initKoin
import com.hicham.wcstoreapp.util.AndroidLogcatLogger
import com.hicham.wcstoreapp.util.Logger
import org.koin.dsl.bind
import org.koin.dsl.module

class WCStoreApp : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Logger.installLogger(AndroidLogcatLogger())
        }
        val androidContext = module {
            single<Context> { this@WCStoreApp } bind Application::class
        }
        initKoin(androidContext, appModule, viewModelsModule)
    }
}