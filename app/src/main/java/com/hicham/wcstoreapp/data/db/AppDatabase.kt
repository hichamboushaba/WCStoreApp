package com.hicham.wcstoreapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hicham.wcstoreapp.data.db.daos.AddressDao
import com.hicham.wcstoreapp.data.db.daos.CartDao
import com.hicham.wcstoreapp.data.db.daos.CategoryDao
import com.hicham.wcstoreapp.data.db.daos.ProductDao
import com.hicham.wcstoreapp.data.db.entities.*

@Database(
    entities = [ProductEntity::class, CartItemEntity::class, CartEntity::class, AddressEntity::class, CategoryEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun productDao(): ProductDao
    abstract fun addressDao(): AddressDao
    abstract fun categoryDao(): CategoryDao
}
