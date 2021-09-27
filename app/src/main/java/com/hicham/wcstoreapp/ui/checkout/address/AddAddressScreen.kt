package com.hicham.wcstoreapp.ui.checkout.address

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hicham.wcstoreapp.ui.theme.WCStoreAppTheme

@Composable
fun AddAddressScreen(viewModel: AddAddressViewModel) {
    val state by viewModel.uiState.collectAsState()
    AddAddressScreen(
        state = state,
        onFieldEdited = viewModel::onFieldEdited,
        onSaveClicked = viewModel::onSaveClicked
    )
}

@Composable
private fun AddAddressScreen(
    state: AddAddressViewModel.UiState,
    onFieldEdited: (AddAddressViewModel.Field, String) -> Unit = { _, _ -> },
    onSaveClicked: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .scrollable(scrollState, Orientation.Vertical),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier)
        OutlinedTextField(
            value = state.firstName.orEmpty(),
            onValueChange = { onFieldEdited(AddAddressViewModel.Field.FirstName, it) },
            label = {
                Text("First Name")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = TextFieldDefaults.textFieldColors()
        )
        OutlinedTextField(
            value = state.lastName.orEmpty(),
            onValueChange = { onFieldEdited(AddAddressViewModel.Field.LastName, it) },
            label = {
                Text("Last Name")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        OutlinedTextField(
            value = state.phone.orEmpty(),
            onValueChange = { onFieldEdited(AddAddressViewModel.Field.Phone, it) },
            label = {
                Text("Phone")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        OutlinedTextField(
            value = state.street1.orEmpty(),
            onValueChange = { onFieldEdited(AddAddressViewModel.Field.Street1, it) },
            label = {
                Text("Street 1")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        OutlinedTextField(
            value = state.street2.orEmpty(),
            onValueChange = { onFieldEdited(AddAddressViewModel.Field.Street2, it) },
            label = {
                Text("Street 2")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        OutlinedTextField(
            value = state.city.orEmpty(),
            onValueChange = { onFieldEdited(AddAddressViewModel.Field.City, it) },
            label = {
                Text("City")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        OutlinedTextField(
            value = state.state.orEmpty(),
            onValueChange = { onFieldEdited(AddAddressViewModel.Field.State, it) },
            label = {
                Text("State")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        OutlinedTextField(
            value = state.postCode.orEmpty(),
            onValueChange = { onFieldEdited(AddAddressViewModel.Field.PostCode, it) },
            label = {
                Text("Postal Code")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        OutlinedTextField(
            value = state.country.orEmpty(),
            onValueChange = { onFieldEdited(AddAddressViewModel.Field.Country, it) },
            label = {
                Text("Country")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Button(
            onClick = onSaveClicked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "Save Address")
        }
    }
}

@Preview
@Composable
private fun AddAddressPreview() {
    WCStoreAppTheme() {
        AddAddressScreen(state = AddAddressViewModel.UiState())
    }
}