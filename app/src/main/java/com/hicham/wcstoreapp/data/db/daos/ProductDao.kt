package com.hicham.wcstoreapp.data.db.daos

import androidx.paging.PagingSource
import androidx.room.*
import com.hicham.wcstoreapp.data.db.entities.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM ProductEntity ORDER BY name ASC")
    fun getAll(): Flow<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(vararg productEntity: ProductEntity)

    @Query("SELECT * FROM ProductEntity WHERE id = :productId")
    suspend fun getProduct(productId: Long): ProductEntity?

    @Query("SELECT * FROM ProductEntity ORDER BY name ASC")
    fun pagingSource(): PagingSource<Int, ProductEntity>

    @Query("DELETE FROM ProductEntity")
    suspend fun clearAll()
}