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
import androidx.compose.ui.focus.*
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
import com.hicham.wcstoreapp.ui.Effect
import com.hicham.wcstoreapp.ui.common.InputField
import com.hicham.wcstoreapp.ui.common.RequiredField
import com.hicham.wcstoreapp.ui.common.components.ToolbarScreen
import com.hicham.wcstoreapp.ui.theme.WCStoreAppTheme
import compose.icons.TablerIcons
import compose.icons.tablericons.InfoCircle
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

@Composable
fun AddAddressScreen(viewModel: AddAddressViewModel) {
    val state by viewModel.uiState.collectAsState()

    AddAddressScreen(
        state = state,
        effects = viewModel.effects,
        onFieldEdited = viewModel::onFieldEdited,
        onSaveClicked = viewModel::onSaveClicked,
        onBackClick = viewModel::onBackClicked
    )
}

@Composable
private fun AddAddressScreen(
    state: AddAddressViewModel.UiState,
    effects: Flow<Effect> = emptyFlow(),
    onFieldEdited: (AddAddressViewModel.Field, String) -> Unit = { _, _ -> },
    onBackClick: () -> Unit = {},
    onSaveClicked: () -> Unit = {},
) {
    val fieldsCount = AddAddressViewModel.Field.values().size
    val scrollState = rememberScrollState()
    val focusRequesters = remember {
        List(fieldsCount) { FocusRequester() }
    }

    LaunchedEffect("effects") {
        effects.collect { effect ->
            when (effect) {
                is AddAddressViewModel.FocusOnField -> {
                    val focusRequester =
                        focusRequesters[AddAddressViewModel.Field.values().indexOf(effect.field)]
                    focusRequester.requestFocus()
                }
            }
        }
    }


    ToolbarScreen(title = { Text(text = "Add Address") }, onNavigationClick = onBackClick) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            AddAddressViewModel.Field.values().forEachIndexed { index, field ->
                AddressTextField(
                    inputField = state[field],
                    onValueChange = {
                        onFieldEdited(
                            field,
                            it
                        )
                    },
                    label = field.label,
                    helperText = field.helperText,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = field.keyboardType,
                        imeAction = if (index == fieldsCount - 1) {
                            ImeAction.Done
                        } else {
                            ImeAction.Next
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusOrder(focusRequesters[index]) {
                            if (index < fieldsCount - 1) next = focusRequesters[index + 1]
                        }
                        .padding(horizontal = 16.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))

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
}

private val AddAddressViewModel.Field.label: String
    get() = when (this) {
        AddAddressViewModel.Field.AddressLabel -> "Address Label"
        AddAddressViewModel.Field.FirstName -> "First Name"
        AddAddressViewModel.Field.LastName -> "Last Name"
        AddAddressViewModel.Field.Street1 -> "Street 1"
        AddAddressViewModel.Field.Street2 -> "Street 2"
        AddAddressViewModel.Field.Phone -> "Phone"
        AddAddressViewModel.Field.City -> "City"
        AddAddressViewModel.Field.State -> "State"
        AddAddressViewModel.Field.PostCode -> "Postal Code"
        AddAddressViewModel.Field.Country -> "Country"
    }

private val AddAddressViewModel.Field.keyboardType: KeyboardType
    get() = when (this) {
        AddAddressViewModel.Field.Phone -> KeyboardType.Phone
        else -> KeyboardType.Text
    }

private val AddAddressViewModel.Field.helperText: String
    get() = when (this) {
        AddAddressViewModel.Field.AddressLabel -> "An optional name to identify the address"
        else -> ""
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
private fun AddressTextField(
    label: String,
    inputField: InputField<*>,
    helperText: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
) {
    val isError = inputField.error != null
    val relocationRequester = remember { RelocationRequester() }
    val scope = rememberCoroutineScope()

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
        Text(
            text = if (isError) stringResource(id = inputField.error!!) else helperText,
            color = if (isError) Color.Red else Color.Unspecified,
            style = MaterialTheme.typography.caption,
            modifier = Modifier
                .padding(start = 16.dp)
                .defaultMinSize(minHeight = 24.dp)
        )
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