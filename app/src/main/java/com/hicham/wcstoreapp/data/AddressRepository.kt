package com.hicham.wcstoreapp.data

import com.hicham.wcstoreapp.models.Address
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AddressRepository {
    val savedAddresses: Flow<List<Address>>
    val primaryShippingAddress: Flow<Address?>
    val primaryBillingAddress: Flow<Address?>

    suspend fun addAddress(address: Address)
    suspend fun removeAddress(address: Address)
    suspend fun setPrimaryShippingAddress(address: Address)
    suspend fun setPrimaryBillingAddress(address: Address)
}