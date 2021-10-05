package com.hicham.wcstoreapp.data.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.hicham.wcstoreapp.data.db.entities.AddressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AddressDao {
    @Query("SELECT * FROM AddressEntity")
    fun getSavedAddresses(): Flow<List<AddressEntity>>

    @Insert
    suspend fun insertAddress(addressEntity: AddressEntity)

    @Delete
    suspend fun deleteAddress(addressEntity: AddressEntity)
}