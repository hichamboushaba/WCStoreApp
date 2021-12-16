package com.hicham.wcstoreapp.data.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.hicham.wcstoreapp.data.api.NetworkAddress
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

    @Query(
        """
        SELECT * FROM AddressEntity
        WHERE firstName = :firstName
        AND lastName = :lastName
        AND street1 = :address1
        AND street2 = :address2
        AND postCode = :postCode
        AND city = :city
        AND state = :state
        AND country = :country
        AND phone = :phone
    """
    )
    suspend fun getMatchingAddress(
        firstName: String?,
        lastName: String?,
        address1: String?,
        address2: String?,
        postCode: String?,
        city: String?,
        state: String?,
        country: String?,
        phone: String?
    ): AddressEntity?

    suspend fun getMatchingAddress(networkAddress: NetworkAddress) = with(networkAddress) {
        getMatchingAddress(
            firstName = firstName,
            lastName = lastName,
            address1 = address1,
            address2 = address2,
            postCode = postcode,
            city = city,
            state = state,
            country = country,
            phone = phone
        )
    }
}