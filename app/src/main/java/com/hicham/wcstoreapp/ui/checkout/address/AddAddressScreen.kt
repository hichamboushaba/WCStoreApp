package com.hicham.wcstoreapp.ui.checkout.address

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.RelocationRequester
import androidx.compose.ui.layout.relocationRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ExperimentalAnimatedInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.rememberImeNestedScrollConnection
import com.hicham.wcstoreapp.ui.common.InputField
import com.hicham.wcstoreapp.ui.common.RequiredField
import com.hicham.wcstoreapp.ui.common.components.ToolbarScreen
import com.hicham.wcstoreapp.ui.theme.WCStoreAppTheme
import compose.icons.TablerIcons
import compose.icons.tablericons.InfoCircle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

@OptIn(ExperimentalAnimatedInsets::class)
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
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                ),
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
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
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


/**
 * This composable use a workaround to highlight the focused view when keyboard is displayed.
 * Since Compose doesn't handle it now.
 * See: https://issuetracker.google.com/issues/192043120
 *
 * Even ProvideWindowInsets(windowInsetsAnimationsEnabled = true) doesn't handle it well now
 * when the column is scrollable
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun TextField(
    label: String,
    inputField: InputField<*>,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
) {
    val isError = inputField.error != null
    val relocationRequester = remember { RelocationRequester() }
    val scope = rememberCoroutineScope()

    val localFocusManage = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

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
            singleLine = true,
            keyboardOptions = keyboardOptions,
            keyboardActions = KeyboardActions(
                onNext = {
                    localFocusManage.moveFocus(FocusDirection.Down)
                },
                onDone = {
                    keyboardController?.hide()
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .relocationRequester(relocationRequester)
                .onFocusChanged {
                    if (it.isFocused) {
                        scope.launch {
                            delay(200)
                            relocationRequester.bringIntoView()
                        }
                    }
                },
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