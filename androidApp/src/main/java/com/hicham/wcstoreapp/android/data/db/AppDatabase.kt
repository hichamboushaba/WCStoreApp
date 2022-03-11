package com.hicham.wcstoreapp.android.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hicham.wcstoreapp.android.data.db.daos.AddressDao
import com.hicham.wcstoreapp.android.data.db.daos.CartDao
import com.hicham.wcstoreapp.android.data.db.daos.CategoryDao
import com.hicham.wcstoreapp.android.data.db.daos.ProductDao
import com.hicham.wcstoreapp.android.data.db.entities.*

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
