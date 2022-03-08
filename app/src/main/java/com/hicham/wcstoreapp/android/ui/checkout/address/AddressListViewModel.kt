package com.hicham.wcstoreapp.android.ui.checkout.address

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.hicham.wcstoreapp.android.data.address.AddressRepository
import com.hicham.wcstoreapp.android.models.Address
import com.hicham.wcstoreapp.android.ui.BaseViewModel
import com.hicham.wcstoreapp.android.ui.ShowSnackbar
import com.hicham.wcstoreapp.android.ui.navigation.NavigationManager
import com.hicham.wcstoreapp.android.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
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
        navigationManager.observeResult<Address>(AddAddressViewModel.ADDRESS_RESULT)
            .onStart {
                val defaultAddress = savedStateHandle.get<Address>("address")
                    ?: addressRepository.primaryShippingAddress.first()
                if (defaultAddress != null) emit(defaultAddress)
            }
            .onEach { selectedAddress.value = it }
            .launchIn(viewModelScope)

        // Save current selected address to savedStateHandle
        selectedAddress.onEach {
            savedStateHandle.set("address", it)
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