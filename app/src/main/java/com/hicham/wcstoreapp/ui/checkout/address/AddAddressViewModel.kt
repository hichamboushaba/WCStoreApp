package com.hicham.wcstoreapp.ui.checkout.address

import android.util.Patterns
import androidx.core.util.PatternsCompat
import androidx.lifecycle.viewModelScope
import com.hicham.wcstoreapp.R
import com.hicham.wcstoreapp.data.AddressRepository
import com.hicham.wcstoreapp.models.Address
import com.hicham.wcstoreapp.ui.BaseViewModel
import com.hicham.wcstoreapp.ui.common.InputField
import com.hicham.wcstoreapp.ui.common.OptionalField
import com.hicham.wcstoreapp.ui.common.RequiredField
import com.hicham.wcstoreapp.ui.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@HiltViewModel
class AddAddressViewModel @Inject constructor(
    private val addressRepository: AddressRepository,
    private val navigationManager: NavigationManager
) : BaseViewModel() {
    companion object {
        const val ADDRESS_RESULT = "address"
    }

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
                    label = it.addressLabel.content,
                    firstName = it.firstName.content,
                    lastName = it.lastName.content,
                    street1 = it.street1.content,
                    street2 = it.street2.content,
                    phone = it.phone.content,
                    city = it.city.content,
                    state = it.state.content,
                    postCode = it.postCode.content,
                    country = it.country.content
                )
            }
            addressRepository.addAddress(address)
            navigationManager.navigateBackWithResult(ADDRESS_RESULT, address)
        }
    }

    fun onBackClicked() {
        navigationManager.navigateUp()
    }

    data class UiState(
        val addressLabel: OptionalField = OptionalField(""),
        val firstName: RequiredField = RequiredField(""),
        val lastName: RequiredField = RequiredField(""),
        val street1: RequiredField = RequiredField(""),
        val street2: OptionalField = OptionalField(""),
        val phone: PhoneField = PhoneField(""),
        val city: RequiredField = RequiredField(""),
        val state: OptionalField = OptionalField(""),
        val postCode: RequiredField = RequiredField(""),
        val country: RequiredField = RequiredField("")
    ) {
        val areAllRequiredFieldsValid
            get() = Field.values().all { get(it).isValid }

        operator fun get(field: Field): InputField<*> {
            return when (field) {
                Field.FirstName -> firstName
                Field.LastName -> lastName
                Field.Street1 -> street1
                Field.Street2 -> street2
                Field.Phone -> phone
                Field.City -> city
                Field.State -> state
                Field.PostCode -> postCode
                Field.Country -> country
            }
        }

        fun updateField(field: Field, content: String): UiState {
            return when (field) {
                Field.FirstName -> copy(firstName = firstName.copy(content = content).validate())
                Field.LastName -> copy(lastName = lastName.copy(content = content).validate())
                Field.Street1 -> copy(street1 = street1.copy(content = content).validate())
                Field.Street2 -> copy(street2 = street2.copy(content = content).validate())
                Field.Phone -> copy(phone = phone.copy(content = content).validate())
                Field.City -> copy(city = city.copy(content = content).validate())
                Field.State -> copy(state = state.copy(content = content).validate())
                Field.PostCode -> copy(postCode = postCode.copy(content = content).validate())
                Field.Country -> copy(country = country.copy(content = content).validate())
            }
        }

        fun validateInput() {
            Field.values().forEach {
                get(it).validate()
            }
        }
    }

    @Parcelize
    data class PhoneField(override val content: String) : InputField<PhoneField>(content) {
        override fun validateInternal(): Int? {
            return if (content.isEmpty() || Patterns.PHONE.matcher(content).matches()) null
            else R.string.error_invalid_phone
        }
    }

    enum class Field {
        FirstName, LastName, Street1, Street2, Phone, City, State, PostCode, Country
    }
}