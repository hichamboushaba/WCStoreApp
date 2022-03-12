package com.hicham.wcstoreapp.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module

fun initKoin(vararg appModules: Module): KoinApplication {
    return startKoin {
        modules(*appModules, networkModule, dataModule)
    }
}