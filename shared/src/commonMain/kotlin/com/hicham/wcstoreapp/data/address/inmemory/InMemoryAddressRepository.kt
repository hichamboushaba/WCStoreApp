package com.hicham.wcstoreapp.data.address.inmemory

import com.hicham.wcstoreapp.data.address.AddressRepository
import com.hicham.wcstoreapp.models.Address
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update

class InMemoryAddressRepository : AddressRepository {
    private val _savedAddresses = MutableStateFlow(emptyList<Address>())
    override val savedAddresses: Flow<List<Address>>
        get() = _savedAddresses

    override val primaryShippingAddress: Flow<Address?> = flowOf(null)
    override val primaryBillingAddress: Flow<Address?> = flowOf(null)

    override suspend fun addAddress(address: Address) {
        _savedAddresses.update { it + address }
    }

    override suspend fun removeAddress(address: Address) {
        _savedAddresses.update { it - address }
    }

    override suspend fun setPrimaryShippingAddress(address: Address): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun setPrimaryBillingAddress(address: Address) {
        return
    }

}