package com.hicham.wcstoreapp.android

import android.app.Application
import android.content.Context
import com.hicham.wcstoreapp.android.di.appModule
import com.hicham.wcstoreapp.android.di.viewModelsModule
import com.hicham.wcstoreapp.di.initKoin
import logcat.AndroidLogcatLogger
import logcat.LogPriority
import org.koin.dsl.bind
import org.koin.dsl.module

class WCStoreApp : Application() {
    override fun onCreate() {
        super.onCreate()

        AndroidLogcatLogger.installOnDebuggableApp(this, minPriority = LogPriority.VERBOSE)
        val androidContext = module {
            single<Context> { this@WCStoreApp } bind Application::class
        }
        initKoin(androidContext, appModule, viewModelsModule)
    }
}