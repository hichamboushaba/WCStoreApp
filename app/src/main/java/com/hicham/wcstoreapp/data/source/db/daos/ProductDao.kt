package com.hicham.wcstoreapp.data.source.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hicham.wcstoreapp.data.source.db.entities.ProductEntity
import com.hicham.wcstoreapp.models.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM ProductEntity")
    fun getAll(): Flow<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(vararg productEntity: ProductEntity)

    @Query("SELECT * FROM ProductEntity WHERE id = :productId")
    suspend fun getProduct(productId: Long): ProductEntity?
}