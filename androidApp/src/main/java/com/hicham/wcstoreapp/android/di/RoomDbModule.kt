package com.hicham.wcstoreapp.android.di

import androidx.room.Room
import com.hicham.wcstoreapp.android.data.db.AppDatabase
import org.koin.dsl.module

val roomDbModule = module {
    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java, "database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}