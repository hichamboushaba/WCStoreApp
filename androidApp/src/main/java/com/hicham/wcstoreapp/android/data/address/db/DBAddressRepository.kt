//package com.hicham.wcstoreapp.android.data.address.db
//
//import android.os.Parcel
//import android.os.Parcelable
//import com.hicham.wcstoreapp.android.data.cart.db.CartUpdateService
//import com.hicham.wcstoreapp.android.data.db.AppDatabase
//import com.hicham.wcstoreapp.android.data.db.entities.AddressEntity
//import com.hicham.wcstoreapp.android.di.HiltAppCoroutineScope
//import com.hicham.wcstoreapp.data.address.AddressRepository
//import com.hicham.wcstoreapp.data.api.NetworkUpdateCustomerRequest
//import com.hicham.wcstoreapp.data.api.WooCommerceApi
//import com.hicham.wcstoreapp.data.api.toNetworkAddress
//import com.hicham.wcstoreapp.models.Address
//import com.hicham.wcstoreapp.util.runCatchingNetworkErrors
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.flow.*
//
//class DBAddressRepository(
//    private val database: AppDatabase,
//    @HiltAppCoroutineScope private val appCoroutineScope: CoroutineScope,
//    private val api: WooCommerceApi,
//    private val cartUpdateService: CartUpdateService
//) : AddressRepository, Parcelable {
//    private val addressDao = database.addressDao()
//    private val cartDao = database.cartDao()
//
//    private val _savedAddresses = addressDao.getSavedAddresses()
//        .shareIn(
//            scope = appCoroutineScope,
//            started = SharingStarted.WhileSubscribed(replayExpirationMillis = 0),
//            replay = 1
//        )
//
//    override val savedAddresses = _savedAddresses.map { list -> list.map { it.toAddress() } }
//
//    override val primaryShippingAddress = combine(
//        _savedAddresses,
//        cartDao.observeCart()
//    ) { savedAddresses, cart ->
//        cart?.cartEntity
//            ?.primaryShippingAddress
//            ?.let { primaryAddressId -> savedAddresses.firstOrNull { it.id == primaryAddressId } }
//            ?.toAddress()
//    }
//        .distinctUntilChanged()
//
//    override val primaryBillingAddress = MutableStateFlow(null)
//
//    constructor(parcel: Parcel) : this(
//        TODO("database"),
//        TODO("appCoroutineScope"),
//        TODO("api"),
//        TODO("cartUpdateService")
//    ) {
//    }
//
//    override suspend fun addAddress(address: Address) {
//        addressDao.insertAddress(address.toEntity())
//
//        // Wait until the the data change is reflected
//        _savedAddresses.waitUntil { addresses ->
//            addresses.any { it.isSameAsAddress(address) }
//        }
//    }
//
//    override suspend fun removeAddress(address: Address) {
//        val addressEntity =
//            _savedAddresses.first().first { it.isSameAsAddress(address) }
//        addressDao.deleteAddress(addressEntity)
//
//        // Wait until the the data change is reflected
//        _savedAddresses.waitUntil { addresses ->
//            addresses.none { it.isSameAsAddress(address) }
//        }
//    }
//
//    override suspend fun setPrimaryShippingAddress(address: Address): Result<Unit> =
//        runCatchingNetworkErrors {
//            val addressEntity =
//                _savedAddresses.first().firstOrNull { it.isSameAsAddress(address) }
//            if (addressEntity == null) {
//                addressDao.insertAddress(address.toEntity())
//            }
//
//            val networkCart =
//                api.updateCustomer(
//                    request = NetworkUpdateCustomerRequest(shippingAddress = address.toNetworkAddress())
//                )
//            cartUpdateService.updateCart(networkCart)
//        }
//
//    override suspend fun setPrimaryBillingAddress(address: Address) {
//        TODO("Not yet implemented")
//    }
//
//    private suspend fun Flow<List<AddressEntity>>.waitUntil(predicate: (List<AddressEntity>) -> Boolean) {
//        takeWhile { predicate(it) }.first()
//    }
//
//    private fun Address.toEntity() = AddressEntity(
//        label = label,
//        firstName = firstName,
//        lastName = lastName,
//        street1 = street1,
//        street2 = street2,
//        phone = phone,
//        email = email,
//        city = city,
//        state = state,
//        postCode = postCode,
//        country = country
//    )
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object CREATOR : Parcelable.Creator<DBAddressRepository> {
//        override fun createFromParcel(parcel: Parcel): DBAddressRepository {
//            return DBAddressRepository(parcel)
//        }
//
//        override fun newArray(size: Int): Array<DBAddressRepository?> {
//            return arrayOfNulls(size)
//        }
//    }
//}