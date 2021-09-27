package com.hicham.wcstoreapp.ui.checkout.address

import androidx.lifecycle.viewModelScope
import com.hicham.wcstoreapp.data.AddressRepository
import com.hicham.wcstoreapp.models.Address
import com.hicham.wcstoreapp.ui.BaseViewModel
import com.hicham.wcstoreapp.ui.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddAddressViewModel @Inject constructor(
    private val addressRepository: AddressRepository,
    private val navigationManager: NavigationManager
) :
    BaseViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    fun onFieldEdited(field: Field, content: String) {
        _uiState.update { state ->
            state.updateField(field, content)
        }
    }

    fun onSaveClicked() {
        viewModelScope.launch {
            val address = uiState.value.let {
                Address(
                    it.firstName!!,
                    it.lastName!!,
                    it.street1!!,
                    it.street2,
                    it.phone,
                    it.city!!,
                    it.state,
                    it.postCode!!,
                    it.country!!
                )
            }
            addressRepository.addAddress(address)
            addressRepository.setPrimaryShippingAddress(address)
            navigationManager.navigateUp()
        }
    }

    data class UiState(
        val firstName: String? = null,
        val lastName: String? = null,
        val street1: String? = null,
        val street2: String? = null,
        val phone: String? = null,
        val city: String? = null,
        val state: String? = null,
        val postCode: String? = null,
        val country: String? = null
    ) {
        fun updateField(field: Field, content: String): UiState {
            return when (field) {
                Field.FirstName -> copy(firstName = content)
                Field.LastName -> copy(lastName = content)
                Field.Street1 -> copy(street1 = content)
                Field.Street2 -> copy(street2 = content)
                Field.Phone -> copy(phone = content)
                Field.City -> copy(city = content)
                Field.State -> copy(state = content)
                Field.PostCode -> copy(postCode = content)
                Field.Country -> copy(country = content)
            }
        }
    }

    enum class Field {
        FirstName, LastName, Street1, Street2, Phone, City, State, PostCode, Country
    }
}