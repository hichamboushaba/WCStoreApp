package com.hicham.wcstoreapp.di

import com.hicham.wcstoreapp.data.db.DriverFactory
import org.koin.core.module.Module

actual fun Module.sqlDriver() {
    single { DriverFactory(get()).createDriver() }
}