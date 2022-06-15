package com.hicham.wcstoreapp.di

import com.hicham.wcstoreapp.Database
import com.hicham.wcstoreapp.data.db.*
import com.hicham.wcstoreapp.data.db.daos.AddressDao
import com.hicham.wcstoreapp.data.db.daos.CartDao
import com.hicham.wcstoreapp.data.db.daos.ProductDao
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
            ),
            CartEntityAdapter = CartEntity.Adapter(
                shippingEstimateAdapter = bigDecimalAdapter,
                subtotalAdapter = bigDecimalAdapter,
                taxAdapter = bigDecimalAdapter,
                totalAdapter = bigDecimalAdapter
            ),
            CartItemEntityAdapter = CartItemEntity.Adapter(
                subtotalAdapter = bigDecimalAdapter,
                taxAdapter = bigDecimalAdapter,
                totalAdapter = bigDecimalAdapter
            )
        )
    }

    single { ProductDao(get()) }
    single { CartDao(get()) }
    single { AddressDao(get()) }
}