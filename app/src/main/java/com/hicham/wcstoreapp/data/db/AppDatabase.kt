package com.hicham.wcstoreapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hicham.wcstoreapp.data.db.daos.AddressDao
import com.hicham.wcstoreapp.data.db.daos.CartDao
import com.hicham.wcstoreapp.data.db.daos.CategoryDao
import com.hicham.wcstoreapp.data.db.daos.ProductDao
import com.hicham.wcstoreapp.data.db.entities.AddressEntity
import com.hicham.wcstoreapp.data.db.entities.CartItemEntity
import com.hicham.wcstoreapp.data.db.entities.CategoryEntity
import com.hicham.wcstoreapp.data.db.entities.ProductEntity

@Database(
    entities = [ProductEntity::class, CartItemEntity::class, AddressEntity::class, CategoryEntity::class],
    version = 4
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun productDao(): ProductDao
    abstract fun addressDao(): AddressDao
    abstract fun categoryDao(): CategoryDao
}
