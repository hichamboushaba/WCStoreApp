package com.hicham.wcstoreapp.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module

fun initKoin(appModule: Module): KoinApplication {
    return startKoin {
        modules(appModule, networkModule, dataModule)
    }
}