package com.hicham.wcstoreapp.di

import com.hicham.wcstoreapp.data.address.AddressRepository
import com.hicham.wcstoreapp.data.storeApi.WooCommerceApi
import com.hicham.wcstoreapp.models.Address
import com.hicham.wcstoreapp.ui.NavigationManager
import kotlinx.cinterop.ObjCClass
import kotlinx.cinterop.ObjCProtocol
import kotlinx.cinterop.getOriginalKotlinClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.context.GlobalContext
import org.koin.core.parameter.ParametersHolder
import org.koin.core.qualifier.Qualifier
import org.koin.dsl.module

fun initKoin(navigationManager: NavigationManager) {
    initKoin(appModule, viewModels, module {
        single { navigationManager }
    })

    GlobalScope.launch(Dispatchers.Main) {
        // Refresh nonce
        val api: WooCommerceApi = koin.get()
        api.getCart()

        val addressRepository: AddressRepository = koin.get()
        val currentAddress = addressRepository.primaryBillingAddress.first()

        if (currentAddress == null) {
            val address = Address(
                firstName = "Test",
                lastName = "Test",
                street1 = "Test Street",
                street2 = "",
                phone = "012345678",
                email = "test@mail.com",
                city = "City",
                state = "State",
                postCode = "10000",
                country = "MA"
            )

            addressRepository.addAddress(address)
            addressRepository.setPrimaryShippingAddress(address)
        }
    }
}

private val koin
    get() = GlobalContext.get()

fun get(objCProtocol: ObjCProtocol): Any {
    val klass = getOriginalKotlinClass(objCProtocol)!!
    return koin.get(klass, null)
}

fun get(objCProtocol: ObjCProtocol, qualifier: Qualifier? = null): Any {
    val klass = getOriginalKotlinClass(objCProtocol)!!
    return koin.get(klass, qualifier)
}

fun get(objCClass: ObjCClass): Any {
    return get(objCClass = objCClass, qualifier = null)
}

fun get(objCClass: ObjCClass, qualifier: Qualifier?): Any {
    return get(objCClass = objCClass, qualifier = qualifier, parameters = null)
}

fun get(objCClass: ObjCClass, parameters: List<Any?>?): Any {
    return get(objCClass = objCClass, qualifier = null, parameters = parameters)
}

fun get(
    objCClass: ObjCClass,
    qualifier: Qualifier? = null,
    parameters: List<Any?>? = null
): Any {
    val klass = getOriginalKotlinClass(objCClass)!!
    return koin.get(klass, qualifier, parameters?.let {
        { ParametersHolder(it.toMutableList()) }
    })
}
