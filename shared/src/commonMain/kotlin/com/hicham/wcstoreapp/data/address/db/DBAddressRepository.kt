package com.hicham.wcstoreapp.android.data.address.db

import com.hicham.wcstoreapp.data.address.AddressRepository
import com.hicham.wcstoreapp.data.storeApi.NetworkUpdateCustomerRequest
import com.hicham.wcstoreapp.data.storeApi.WooCommerceApi
import com.hicham.wcstoreapp.data.storeApi.toNetworkAddress
import com.hicham.wcstoreapp.data.cart.db.CartUpdateService
import com.hicham.wcstoreapp.data.db.AddressEntity
import com.hicham.wcstoreapp.data.db.daos.AddressDao
import com.hicham.wcstoreapp.data.db.daos.CartDao
import com.hicham.wcstoreapp.data.db.toDomainModel
import com.hicham.wcstoreapp.models.Address
import com.hicham.wcstoreapp.util.DB
import com.hicham.wcstoreapp.util.runCatchingNetworkErrors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext

class DBAddressRepository(
    private val appCoroutineScope: CoroutineScope,
    private val addressDao: AddressDao,
    private val cartDao: CartDao,
    private val api: WooCommerceApi,
    private val cartUpdateService: CartUpdateService,
) : AddressRepository {
    private val _savedAddresses = addressDao.getSavedAddresses()
        .shareIn(
            scope = appCoroutineScope + Dispatchers.Main,
            started = SharingStarted.WhileSubscribed(replayExpirationMillis = 0),
            replay = 1
        )

    override val savedAddresses = _savedAddresses.map { list -> list.map { it.toDomainModel() } }

    override val primaryShippingAddress = combine(
        _savedAddresses,
        cartDao.observeCart()
    ) { savedAddresses, cart ->
        cart?.cartEntity
            ?.primaryShippingAddress
            ?.let { primaryAddressId -> savedAddresses.firstOrNull { it.id == primaryAddressId } }
            ?.toDomainModel()
    }
        .distinctUntilChanged()

    override val primaryBillingAddress = MutableStateFlow(null)

    override suspend fun addAddress(address: Address) {
        withContext(Dispatchers.DB) {
            addressDao.insertAddress(address.toEntity())
        }

        // Wait until the the data change is reflected
        _savedAddresses.waitUntil { addresses ->
            addresses.any { it.isSameAsAddress(address) }
        }
    }

    override suspend fun removeAddress(address: Address) {
        val addressEntity = _savedAddresses.first().first { it.isSameAsAddress(address) }
        withContext(Dispatchers.DB) {
            addressDao.deleteAddress(addressEntity)
        }
        // Wait until the the data change is reflected
        _savedAddresses.waitUntil { addresses ->
            addresses.none { it.isSameAsAddress(address) }
        }
    }

    override suspend fun setPrimaryShippingAddress(address: Address): Result<Unit> =
        runCatchingNetworkErrors {
            val addressEntity =
                _savedAddresses.first().firstOrNull { it.isSameAsAddress(address) }
            if (addressEntity == null) {
                addressDao.insertAddress(address.toEntity())
            }

            val networkCart =
                api.updateCustomer(
                    request = NetworkUpdateCustomerRequest(shippingAddress = address.toNetworkAddress())
                )
            cartUpdateService.updateCart(networkCart)
        }

    override suspend fun setPrimaryBillingAddress(address: Address) {
        TODO("Not yet implemented")
    }

    private suspend fun Flow<List<AddressEntity>>.waitUntil(predicate: (List<AddressEntity>) -> Boolean) {
        filter { predicate(it) }.first()
    }

    private fun Address.toEntity() = AddressEntity(
        id = -1,
        label = label,
        firstName = firstName,
        lastName = lastName,
        street1 = street1,
        street2 = street2,
        phone = phone,
        email = email,
        city = city,
        state = state,
        postCode = postCode,
        country = country
    )

    private fun AddressEntity.isSameAsAddress(address: Address): Boolean {
        return toDomainModel() == address
    }
}