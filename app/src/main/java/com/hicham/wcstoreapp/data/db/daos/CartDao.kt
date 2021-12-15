package com.hicham.wcstoreapp.data.db.daos

import androidx.room.*
import com.hicham.wcstoreapp.data.db.entities.CartEntity
import com.hicham.wcstoreapp.data.db.entities.CartItemEntity
import com.hicham.wcstoreapp.data.db.entities.CartItemWithProduct
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Dao
interface CartDao {
    @Transaction
    @Query(
        "SELECT * FROM CartEntity " +
                "JOIN CartItemEntity where id = ${CartEntity.ID}"
    )
    fun getCartInternal(): Flow<Map<CartEntity, List<CartItemWithProduct>>>

    fun getCart() = getCartInternal().map {
        if (it.isEmpty()) Pair(null, emptyList())
        else Pair(it.keys.first(), it[it.keys.first()].orEmpty())
    }

    @Transaction
    @Query("SELECT * FROM CartItemEntity")
    fun getCartItemsWithProducts(): Flow<List<CartItemWithProduct>>

    @Query("SELECT * FROM CartItemEntity")
    suspend fun getCartItems(): List<CartItemEntity>

    @Query("SELECT * FROM CartItemEntity WHERE productId = :productId")
    suspend fun getCartItem(productId: Long): CartItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCart(cart: CartEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(vararg cartItemEntity: CartItemEntity)

    @Query("DELETE FROM CartItemEntity WHERE productId = :productId")
    suspend fun deleteCartItemForProductId(productId: Long)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateItem(cartItemEntity: CartItemEntity)

    @Query("DELETE FROM CartItemEntity")
    suspend fun clear()
}