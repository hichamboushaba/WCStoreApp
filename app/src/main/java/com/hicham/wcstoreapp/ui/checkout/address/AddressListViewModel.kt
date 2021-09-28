package com.hicham.wcstoreapp.ui.checkout.address

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.hicham.wcstoreapp.data.AddressRepository
import com.hicham.wcstoreapp.models.Address
import com.hicham.wcstoreapp.ui.BaseViewModel
import com.hicham.wcstoreapp.ui.navigation.NavigationManager
import com.hicham.wcstoreapp.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val addressRepository: AddressRepository,
    private val navigationManager: NavigationManager
) :
    BaseViewModel() {
    val items: Flow<List<AddressItemUiModel>>

    private val selectedAddress = MutableStateFlow<Address?>(null)

    init {
        items = combine(
            addressRepository.savedAddresses, selectedAddress
        ) { addressList, selectedAddress ->
            addressList.map {
                AddressItemUiModel(address = it, isSelected = it == selectedAddress)
            }
        }

        // Listen to changes of primaryShippingAddress
        addressRepository.primaryShippingAddress
            .distinctUntilChanged()
            .onEach { selectedAddress.value = it }
            .launchIn(viewModelScope)
    }

    fun onAddressClicked(address: Address) {
        selectedAddress.value = address
    }

    fun onAddAddressClicked() {
        navigationManager.navigate(Screen.AddAddress.route)
    }

    fun onSaveClicked() {
        viewModelScope.launch {
            addressRepository.setPrimaryShippingAddress(selectedAddress.value!!)
            navigationManager.navigateUp()
        }
    }

    fun onBackClicked() {
        navigationManager.navigateUp()
    }

    data class AddressItemUiModel(
        val address: Address,
        val isSelected: Boolean
    )
}