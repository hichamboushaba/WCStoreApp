package com.hicham.wcstoreapp.ui.checkout

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hicham.wcstoreapp.models.Address
import com.hicham.wcstoreapp.ui.components.CartTotals

@Composable
fun CheckoutScreen(viewModel: CheckoutViewModel) {
    val state by viewModel.uiState.collectAsState(CheckoutViewModel.UiState())
    CheckoutScreen(
        state = state,
        onEditShippingAddress = viewModel::onEditShippingAddressClicked
    )
}

@Composable
private fun CheckoutScreen(
    state: CheckoutViewModel.UiState,
    onEditShippingAddress: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    Column(
        Modifier
            .fillMaxSize()
            .scrollable(scrollState, orientation = Orientation.Vertical)
            .padding(top = 16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    text = "Shipping Address", style = MaterialTheme.typography.subtitle1
                )
                Spacer(modifier = Modifier.size(16.dp))
                if (state.shippingAddress == null) {
                    OutlinedButton(
                        onClick = onEditShippingAddress, modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text("Add new address")
                    }
                } else {
                    Text(
                        text = state.shippingAddress.formatAddress(),
                        style = MaterialTheme.typography.body1
                    )
                    Spacer(Modifier.size(16.dp))
                    OutlinedButton(
                        onClick = onEditShippingAddress, modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text("Change address")
                    }
                }
            }
        }
        when {
            state.isBillingSameAsShippingAddress -> {
                Row(modifier = Modifier.padding(16.dp)) {
                    Checkbox(checked = true, onCheckedChange = {/*TODO*/ })
                    Text(text = "Billing Address same as shipping")
                }
            }
            else -> {
                Spacer(modifier = Modifier.size(32.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            text = "Billing Address", style = MaterialTheme.typography.subtitle1
                        )
                        Spacer(modifier = Modifier.size(16.dp))
                        Text(
                            text = state.billingAddress?.formatAddress() ?: AnnotatedString(""),
                            style = MaterialTheme.typography.body1
                        )
                        Spacer(Modifier.size(16.dp))
                        OutlinedButton(
                            onClick = { /*TODO*/ }, modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            Text("Change address")
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.size(32.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Payment")
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        CartTotals(
            subtotal = state.subtotalFormatted,
            total = state.totalFormatted,
            shippingCost = state.shippingCost,
            buttonLabel = "Place Order",
            onButtonClick = { /*TODO*/ })
    }
}

@Preview
@Composable
private fun CheckoutPreview() {
    CheckoutScreen(
        CheckoutViewModel.UiState(
            shippingAddress = Address(
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
            )
        )
    )
}