package com.hicham.wcstoreapp.data.source.db.daos

import androidx.room.*
import com.hicham.wcstoreapp.data.source.db.entities.CartItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Transaction
    @Query("SELECT * FROM CartItemEntity")
    fun getCartItems(): Flow<List<CartItemEntity>>

    @Query("SELECT * FROM CartItemEntity WHERE productId = :productId")
    suspend fun getCartItem(productId: Long): CartItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(vararg cartItemEntity: CartItemEntity)

    @Delete
    suspend fun deleteItem(cartItemEntity: CartItemEntity)

    @Query("DELETE FROM CartItemEntity WHERE productId = :productId")
    suspend fun deleteCartItemForProductId(productId: Long)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateItem(cartItemEntity: CartItemEntity)
}