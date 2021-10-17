package com.hicham.wcstoreapp.data.address.db

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.hicham.wcstoreapp.data.address.AddressRepository
import com.hicham.wcstoreapp.data.db.AppDatabase
import com.hicham.wcstoreapp.data.db.entities.AddressEntity
import com.hicham.wcstoreapp.di.AppCoroutineScope
import com.hicham.wcstoreapp.models.Address
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import logcat.logcat
import javax.inject.Inject

private const val PRIMARY_ADDRESS_KEY = "primary-address"

class DBAddressRepository @Inject constructor(
    private val database: AppDatabase,
    private val dataStore: DataStore<Preferences>,
    @AppCoroutineScope private val appCoroutineScope: CoroutineScope
) : AddressRepository {
    private val addressDao = database.addressDao()

    private val _savedAddresses = addressDao.getSavedAddresses()
        .shareIn(
            scope = appCoroutineScope,
            started = SharingStarted.WhileSubscribed(replayExpirationMillis = 0),
            replay = 1
        )

    override val savedAddresses = _savedAddresses.map { list -> list.map { it.toAddress() } }

    override val primaryShippingAddress = combine(
        _savedAddresses,
        dataStore.data.map { it[longPreferencesKey(PRIMARY_ADDRESS_KEY)] }
    ) { savedAddresses, primaryAddress ->
        primaryAddress?.let { savedAddresses.firstOrNull { it.id == primaryAddress } }
            ?.toAddress()
    }
        .distinctUntilChanged()

    override val primaryBillingAddress = MutableStateFlow(null)

    override suspend fun addAddress(address: Address) {
        with(address) {
            addressDao.insertAddress(
                AddressEntity(
                    label = label,
                    firstName = firstName,
                    lastName = lastName,
                    street1 = street1,
                    street2 = street2,
                    phone = phone,
                    city = city,
                    state = state,
                    postCode = postCode,
                    country = country
                )
            )
        }
    }

    override suspend fun removeAddress(address: Address) {
        val addressEntity =
            _savedAddresses.first().first { it.isSameAsAddress(address) }
        addressDao.deleteAddress(addressEntity)
    }

    override suspend fun setPrimaryShippingAddress(address: Address) {
        val addresses = _savedAddresses.first()
        logcat {
            addresses.toString()
        }
        val addressEntity =
            _savedAddresses.first().first { it.isSameAsAddress(address) }
        dataStore.edit {
            it[longPreferencesKey(PRIMARY_ADDRESS_KEY)] = addressEntity.id
        }
    }

    override suspend fun setPrimaryBillingAddress(address: Address) {
        TODO("Not yet implemented")
    }
}