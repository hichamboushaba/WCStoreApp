package com.hicham.wcstoreapp.data.source.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hicham.wcstoreapp.data.source.db.daos.CartDao
import com.hicham.wcstoreapp.data.source.db.daos.ProductDao
import com.hicham.wcstoreapp.data.source.db.entities.CartItemEntity
import com.hicham.wcstoreapp.data.source.db.entities.ProductEntity

@Database(entities = [ProductEntity::class, CartItemEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun productDao(): ProductDao
}
