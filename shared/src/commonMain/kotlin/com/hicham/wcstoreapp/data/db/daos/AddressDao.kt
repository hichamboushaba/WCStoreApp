package com.hicham.wcstoreapp.data.db.daos

import com.hicham.wcstoreapp.Database
import com.hicham.wcstoreapp.data.api.NetworkAddress
import com.hicham.wcstoreapp.data.db.AddressEntity
import com.hicham.wcstoreapp.data.db.AddressEntityQueries
import com.hicham.wcstoreapp.util.DB
import com.squareup.sqldelight.Transacter
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class AddressDao(private val database: Database) : Transacter by database {
    private val addressQueries = database.addressEntityQueries

    fun getSavedAddresses(): Flow<List<AddressEntity>> = addressQueries.selectAll()
        .asFlow()
        .mapToList()

    fun insertAddress(addressEntity: AddressEntity) = addressQueries.insert(addressEntity)

    suspend fun getMatchingAddress(networkAddress: NetworkAddress) =
        withContext(Dispatchers.Default) {
            with(networkAddress) {
                addressQueries.getMatchingAddress(
                    firstName = firstName,
                    lastName = lastName,
                    address1 = address1,
                    address2 = address2,
                    postCode = postcode,
                    city = city,
                    state = state,
                    country = country,
                    phone = phone
                ).executeAsOneOrNull()
            }
        }
}