package com.hicham.wcstoreapp.di

import com.hicham.wcstoreapp.Database
import com.hicham.wcstoreapp.data.db.ProductEntity
import com.hicham.wcstoreapp.data.db.bigDecimalAdapter
import com.hicham.wcstoreapp.data.db.listOfStringsAdapter
import org.koin.core.module.Module
import org.koin.dsl.module

expect fun Module.sqlDriver()

val dbModule = module {
    sqlDriver()
    single {
        Database(
            driver = get(),
            ProductEntityAdapter = ProductEntity.Adapter(
                imagesAdapter = listOfStringsAdapter,
                priceAdapter = bigDecimalAdapter,
                regularPriceAdapter = bigDecimalAdapter,
                salePriceAdapter = bigDecimalAdapter
            )
        )
    }
}