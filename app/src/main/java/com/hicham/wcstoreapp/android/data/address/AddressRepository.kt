package com.hicham.wcstoreapp.android.data.address

import com.hicham.wcstoreapp.android.models.Address
import kotlinx.coroutines.flow.Flow

interface AddressRepository {
    val savedAddresses: Flow<List<Address>>
    val primaryShippingAddress: Flow<Address?>
    val primaryBillingAddress: Flow<Address?>

    suspend fun addAddress(address: Address)
    suspend fun removeAddress(address: Address)
    suspend fun setPrimaryShippingAddress(address: Address): Result<Unit>
    suspend fun setPrimaryBillingAddress(address: Address)
}