package com.hicham.wcstoreapp.android.ui.checkout.address

import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.hicham.wcstoreapp.android.R
import com.hicham.wcstoreapp.android.ui.BaseViewModel
import com.hicham.wcstoreapp.android.ui.Effect
import com.hicham.wcstoreapp.android.ui.common.InputField
import com.hicham.wcstoreapp.android.ui.common.OptionalField
import com.hicham.wcstoreapp.android.ui.common.RequiredField
import com.hicham.wcstoreapp.android.ui.navigation.NavigationManager
import com.hicham.wcstoreapp.data.address.AddressRepository
import com.hicham.wcstoreapp.models.Address
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import java.util.*
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

    init {
        uiState.onEach {
            println(it)
        }.launchIn(viewModelScope)
    }

    fun onFieldEdited(field: Field, content: String) {
        _uiState.update { state ->
            state.updateField(field, content)
        }
    }

    fun onSaveClicked() {
        _uiState.update { state ->
            state.validateInput()
        }
        uiState.value.let { state ->
            if (!state.areAllRequiredFieldsValid) {
                val firstNonValidField = Field.values().first { !state[it].isValid }
                triggerEffect(FocusOnField(firstNonValidField))
                return@let
            }

            // Check country
            val countryCode = Locale.getISOCountries()
                .firstOrNull { Locale("", it).displayCountry == state.country.content } ?: TODO()

            viewModelScope.launch {
                val address = Address(
                    label = state.addressLabel.content,
                    firstName = state.firstName.content,
                    lastName = state.lastName.content,
                    street1 = state.street1.content,
                    street2 = state.street2.content,
                    phone = state.phone.content,
                    email = state.email.content,
                    city = state.city.content,
                    state = state.state.content,
                    postCode = state.postCode.content,
                    country = countryCode
                )
                addressRepository.addAddress(address)
                navigationManager.navigateBackWithResult(ADDRESS_RESULT, address)
            }
        }
    }

    fun onBackClicked() {
        if (uiState.value.hasChanges()) {
            _uiState.update { state -> state.copy(showDiscardChangesDialog = true) }
        } else {
            navigationManager.navigateUp()
        }
    }

    fun onDiscardDialogDismissed() {
        _uiState.update { state -> state.copy(showDiscardChangesDialog = false) }
    }

    fun onDiscardChangesClicked() {
        navigationManager.navigateUp()
    }

    data class UiState(
        val addressLabel: OptionalField = OptionalField(""),
        val firstName: RequiredField = RequiredField(""),
        val lastName: RequiredField = RequiredField(""),
        val street1: RequiredField = RequiredField(""),
        val street2: OptionalField = OptionalField(""),
        val phone: PhoneField = PhoneField(""),
        val email: RequiredField = RequiredField(""),
        val city: RequiredField = RequiredField(""),
        val state: RequiredField = RequiredField(""),
        val postCode: RequiredField = RequiredField(""),
        val country: RequiredField = RequiredField(""),
        val showDiscardChangesDialog: Boolean = false
    ) {
        val areAllRequiredFieldsValid
            get() = Field.values().all { get(it).isValid }

        operator fun get(field: Field): InputField<*> {
            return when (field) {
                Field.AddressLabel -> addressLabel
                Field.FirstName -> firstName
                Field.LastName -> lastName
                Field.Street1 -> street1
                Field.Street2 -> street2
                Field.Phone -> phone
                Field.Email -> email
                Field.City -> city
                Field.State -> state
                Field.PostCode -> postCode
                Field.Country -> country
            }
        }

        fun updateField(field: Field, content: String): UiState {
            return when (field) {
                Field.AddressLabel -> copy(
                    addressLabel = addressLabel.copy(content = content).validate()
                )
                Field.FirstName -> copy(firstName = firstName.copy(content = content).validate())
                Field.LastName -> copy(lastName = lastName.copy(content = content).validate())
                Field.Street1 -> copy(street1 = street1.copy(content = content).validate())
                Field.Street2 -> copy(street2 = street2.copy(content = content).validate())
                Field.Phone -> copy(phone = phone.copy(content = content).validate())
                Field.Email -> copy(email = email.copy(content = content).validate())
                Field.City -> copy(city = city.copy(content = content).validate())
                Field.State -> copy(state = state.copy(content = content).validate())
                Field.PostCode -> copy(postCode = postCode.copy(content = content).validate())
                Field.Country -> copy(country = country.copy(content = content).validate())
            }
        }

        fun validateInput(): UiState {
            return copy(
                firstName = firstName.validate(),
                lastName = lastName.validate(),
                street1 = street1.validate(),
                street2 = street2.validate(),
                phone = phone.validate(),
                city = city.validate(),
                state = state.validate(),
                postCode = postCode.validate(),
                country = country.validate(),
            )
        }

        fun hasChanges(): Boolean = Field.values().any { get(it).content.isNotEmpty() }
    }

    @Parcelize
    data class PhoneField(override val content: String) : InputField<PhoneField>(content) {
        override fun validateInternal(): Int? {
            return if (content.isEmpty() || Patterns.PHONE.matcher(content).matches()) null
            else R.string.error_invalid_phone
        }
    }

    enum class Field {
        AddressLabel, FirstName, LastName, Phone, Email, Street1, Street2, City, State, PostCode, Country
    }

    data class FocusOnField(val field: Field) : Effect()
}