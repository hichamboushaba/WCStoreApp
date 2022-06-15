package com.hicham.wcstoreapp.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

private val coroutineModule = module {
    single<CoroutineScope>(AppCoroutineScopeQualifier) { GlobalScope }
}

fun initKoin(vararg appModules: Module): KoinApplication {
    return startKoin {
        modules(*appModules, coroutineModule, networkModule, dataModule, dbModule)
    }
}