package com.hicham.wcstoreapp.ui.checkout.address

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hicham.wcstoreapp.models.Address
import com.hicham.wcstoreapp.ui.components.ToolbarScreen

@Composable
fun AddressListScreen(viewModel: AddressListViewModel) {
    val items by viewModel.items.collectAsState(initial = emptyList())
    AddressListScreen(
        items = items,
        onAddressClicked = viewModel::onAddressClicked,
        onAddAddressClicked = viewModel::onAddAddressClicked,
        onSaveClicked = viewModel::onSaveClicked,
        onBack = viewModel::onBackClicked
    )
}

@Composable
private fun AddressListScreen(
    items: List<AddressListViewModel.AddressItemUiModel>,
    onAddressClicked: (Address) -> Unit = {},
    onAddAddressClicked: () -> Unit = {},
    onSaveClicked: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    ToolbarScreen(
        title = { Text(text = "Shipping Address") },
        onNavigationClick = onBack
    ) { paddingValues ->
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
                    firstName = "first",
                    lastName = "last",
                    street1 = "street",
                    street2 = null,
                    phone = null,
                    city = "city",
                    state = "state",
                    country = "country",
                    postCode = "postCode"
                ),
                isSelected = true
            ),
            AddressListViewModel.AddressItemUiModel(
                Address(
                    firstName = "first",
                    lastName = "last",
                    street1 = "street",
                    street2 = null,
                    phone = null,
                    city = "city",
                    state = "state",
                    country = "country",
                    postCode = "postCode"
                ),
                isSelected = false
            )
        )
    )
}