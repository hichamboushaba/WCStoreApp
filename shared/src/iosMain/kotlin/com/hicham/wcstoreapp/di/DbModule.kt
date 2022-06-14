package com.hicham.wcstoreapp.di

import com.hicham.wcstoreapp.Database
import com.hicham.wcstoreapp.data.db.DriverFactory
import org.koin.core.module.Module

actual fun Module.db() {
    single {
        val driverFactory = DriverFactory()
        Database(driverFactory.createDriver())
    }
}