package com.hicham.wcstoreapp.ui.checkout.address

import com.hicham.wcstoreapp.data.address.AddressRepository
import com.hicham.wcstoreapp.models.Address
import com.hicham.wcstoreapp.ui.BaseViewModel
import com.hicham.wcstoreapp.ui.NavigationManager
import com.hicham.wcstoreapp.ui.ShowSnackbar
import com.hicham.wcstoreapp.ui.navigation.Screen
import com.hicham.wcstoreapp.ui.observeResultAsFlow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AddressListViewModel(
//    private val savedStateHandle: SavedStateHandle,
    private val addressRepository: AddressRepository,
    private val navigationManager: NavigationManager
) : BaseViewModel() {
    val items: Flow<List<AddressItemUiModel>>

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val selectedAddress = MutableStateFlow<Address?>(null)

    init {
        items = combine(
            addressRepository.savedAddresses, selectedAddress
        ) { addressList, selectedAddress ->
            addressList.map {
                AddressItemUiModel(address = it, isSelected = it == selectedAddress)
            }
        }

        observeSelectedAddress()
    }

    private fun observeSelectedAddress() {
        // Observe navigation result
        navigationManager.observeResultAsFlow<Address>(AddAddressViewModel.ADDRESS_RESULT)
            .onStart {
                val defaultAddress = //savedStateHandle.get<Address>("address")
                     addressRepository.primaryShippingAddress.first()
                if (defaultAddress != null) emit(defaultAddress)
            }
            .onEach { selectedAddress.value = it }
            .launchIn(viewModelScope)

        // Save current selected address to savedStateHandle
        selectedAddress.onEach {
            //savedStateHandle.set("address", it)
        }.launchIn(viewModelScope)
    }

    fun onAddressClicked(address: Address) {
        selectedAddress.value = address
    }

    fun onAddAddressClicked() {
        navigationManager.navigate(Screen.AddAddress.route)
    }

    fun onSaveClicked() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = addressRepository.setPrimaryShippingAddress(selectedAddress.value!!)
            _isLoading.value = false
            result.fold(
                onSuccess = { navigationManager.navigateUp() },
                onFailure = { triggerEffect(ShowSnackbar("Updating the selected address failed")) }
            )
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