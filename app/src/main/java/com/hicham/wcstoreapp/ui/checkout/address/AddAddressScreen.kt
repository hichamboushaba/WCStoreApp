package com.hicham.wcstoreapp.ui.checkout.address

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hicham.wcstoreapp.ui.common.InputField
import com.hicham.wcstoreapp.ui.common.RequiredField
import com.hicham.wcstoreapp.ui.common.components.ToolbarScreen
import com.hicham.wcstoreapp.ui.theme.WCStoreAppTheme
import compose.icons.TablerIcons
import compose.icons.tablericons.InfoCircle

@Composable
fun AddAddressScreen(viewModel: AddAddressViewModel) {
    val state by viewModel.uiState.collectAsState()
    AddAddressScreen(
        state = state,
        onFieldEdited = viewModel::onFieldEdited,
        onSaveClicked = viewModel::onSaveClicked,
        onBackClick = viewModel::onBackClicked
    )
}

@Composable
private fun AddAddressScreen(
    state: AddAddressViewModel.UiState,
    onFieldEdited: (AddAddressViewModel.Field, String) -> Unit = { _, _ -> },
    onBackClick: () -> Unit = {},
    onSaveClicked: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    ToolbarScreen(title = { Text(text = "Add Address") }, onNavigationClick = onBackClick) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                inputField = state.firstName,
                onValueChange = {
                    onFieldEdited(
                        AddAddressViewModel.Field.FirstName,
                        it
                    )
                },
                label = "First Name",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            TextField(
                inputField = state.lastName,
                onValueChange = {
                    onFieldEdited(
                        AddAddressViewModel.Field.LastName,
                        it
                    )
                },
                label = "Last Name",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            TextField(
                inputField = state.phone,
                onValueChange = { onFieldEdited(AddAddressViewModel.Field.Phone, it) },
                label = "Phone",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            TextField(
                inputField = state.street1,
                onValueChange = {
                    onFieldEdited(
                        AddAddressViewModel.Field.Street1,
                        it
                    )
                },
                label = "Street 1",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            TextField(
                inputField = state.street2,
                onValueChange = {
                    onFieldEdited(
                        AddAddressViewModel.Field.Street2,
                        it
                    )
                },
                label = "Street 2",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            TextField(
                inputField = state.city,
                onValueChange = { onFieldEdited(AddAddressViewModel.Field.City, it) },
                label = "City",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            TextField(
                inputField = state.state,
                onValueChange = { onFieldEdited(AddAddressViewModel.Field.State, it) },
                label = "State",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            TextField(
                inputField = state.postCode,
                onValueChange = {
                    onFieldEdited(
                        AddAddressViewModel.Field.PostCode,
                        it
                    )
                },
                label = "Postal Code",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            TextField(
                inputField = state.country,
                onValueChange = {
                    onFieldEdited(
                        AddAddressViewModel.Field.Country,
                        it
                    )
                },
                label = "Country",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onSaveClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = state.areAllRequiredFieldsValid
            ) {
                Text(text = "Save Address")
            }
        }
    }
}

@Composable
private fun TextField(
    label: String,
    inputField: InputField<*>,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    val isError = inputField.error != null
    Column(modifier = modifier) {
        OutlinedTextField(
            value = inputField.content,
            onValueChange = onValueChange,
            isError = isError,
            trailingIcon = {
                if (isError) Icon(
                    imageVector = TablerIcons.InfoCircle,
                    contentDescription = "",
                    tint = Color.Red
                )
            },
            label = {
                Text(label)
            },
            keyboardOptions = keyboardOptions,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(),
        )
        if (isError) {
            Text(
                text = stringResource(id = inputField.error!!),
                color = Color.Red,
                modifier = Modifier.defaultMinSize(minHeight = 24.dp)
            )
        } else {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview
@Composable
private fun AddAddressPreview() {
    WCStoreAppTheme() {
        AddAddressScreen(
            state = AddAddressViewModel.UiState(
                firstName = RequiredField("").validate()
            )
        )
    }
}