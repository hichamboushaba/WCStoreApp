package com.hicham.wcstoreapp.ui.checkout.address

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.hicham.wcstoreapp.models.Address
import com.hicham.wcstoreapp.ui.Effect
import com.hicham.wcstoreapp.ui.ShowSnackbar
import com.hicham.wcstoreapp.ui.common.components.ToolbarScreen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun AddressListScreen(viewModel: AddressListViewModel, scaffoldState: ScaffoldState) {
    val items by viewModel.items.collectAsState(initial = emptyList())
    val isLoading by viewModel.isLoading.collectAsState()
    AddressListScreen(
        items = items,
        isLoading = isLoading,
        effects = viewModel.effects,
        onAddressClicked = viewModel::onAddressClicked,
        onAddAddressClicked = viewModel::onAddAddressClicked,
        onSaveClicked = viewModel::onSaveClicked,
        onBack = viewModel::onBackClicked,
        scaffoldState = scaffoldState
    )
}

@Composable
private fun AddressListScreen(
    items: List<AddressListViewModel.AddressItemUiModel>,
    isLoading: Boolean = false,
    effects: Flow<Effect> = emptyFlow(),
    onAddressClicked: (Address) -> Unit = {},
    onAddAddressClicked: () -> Unit = {},
    onSaveClicked: () -> Unit = {},
    onBack: () -> Unit = {},
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    LaunchedEffect("effects") {
        effects.collect { effect ->
            when (effect) {
                is ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    ToolbarScreen(
        title = { Text(text = "Shipping Address") },
        onNavigationClick = onBack
    ) { paddingValues ->
        if (isLoading) {
            Dialog(
                onDismissRequest = {},
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                )
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(100.dp)
                        .background(
                            MaterialTheme.colors.surface,
                            shape = MaterialTheme.shapes.small
                        )
                ) {
                    CircularProgressIndicator()
                }
            }
        }
        Column(
            Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedButton(onClick = onAddAddressClicked) {
                Text(text = "Add new Address")
            }
            Spacer(modifier = Modifier.size(32.dp))
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(items) {
                    AddressCard(
                        model = it,
                        onClicked = { onAddressClicked(it.address) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.size(24.dp))
            Button(
                onClick = onSaveClicked,
                modifier = Modifier.fillMaxWidth(),
                enabled = items.any { it.isSelected }
            ) {
                Text(text = "Use Selected Address")
            }
        }
    }
}

@Composable
private fun AddressCard(
    model: AddressListViewModel.AddressItemUiModel,
    onClicked: () -> Unit,
    modifier: Modifier
) {
    Card(
        modifier = modifier.clickable(onClick = onClicked),
        border = if (model.isSelected) BorderStroke(1.dp, MaterialTheme.colors.primary) else null
    ) {
        Text(
            text = model.address.formatAddress(),
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview
@Composable
private fun PreviewAddressListScreen() {
    AddressListScreen(
        items = listOf(
            AddressListViewModel.AddressItemUiModel(
                Address(
                    label = "Home",
                    firstName = "first",
                    lastName = "last",
                    street1 = "street",
                    street2 = null,
                    phone = null,
                    city = "city",
                    state = "state",
                    postCode = "postCode",
                    country = "country",
                ),
                isSelected = true
            ),
            AddressListViewModel.AddressItemUiModel(
                Address(
                    label = "Work",
                    firstName = "first",
                    lastName = "last",
                    street1 = "street",
                    street2 = null,
                    phone = null,
                    city = "city",
                    state = "state",
                    postCode = "postCode",
                    country = "country",
                ),
                isSelected = false
            )
        )
    )
}