package com.hicham.wcstoreapp.android.ui.checkout

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hicham.wcstoreapp.android.models.Address
import com.hicham.wcstoreapp.android.models.PaymentMethod
import com.hicham.wcstoreapp.android.ui.ShowSnackbar
import com.hicham.wcstoreapp.android.ui.common.components.CartTotals
import com.hicham.wcstoreapp.android.ui.common.components.ErrorView
import com.hicham.wcstoreapp.android.ui.common.components.IndeterminateLoadingDialog
import com.hicham.wcstoreapp.android.ui.title
import kotlinx.coroutines.flow.collect

@Composable
fun CheckoutScreen(viewModel: CheckoutViewModel, scaffoldState: ScaffoldState) {
    val state by viewModel.uiState.collectAsState(CheckoutViewModel.UiState())

    LaunchedEffect("effects") {
        viewModel.effects.collect { effect ->
            when (effect) {
                is ShowSnackbar -> scaffoldState.snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    CheckoutScreen(
        state = state,
        onRetry = viewModel::onRetryClicked,
        onEditShippingAddress = viewModel::onEditShippingAddressClicked,
        onChangePayment = viewModel::onChangePaymentMethodClicked,
        onPaymentMethodSelected = viewModel::onPaymentMethodSelected,
        onPlaceOrder = viewModel::onPlacedOrderClicked
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun CheckoutScreen(
    state: CheckoutViewModel.UiState,
    onRetry: () -> Unit = {},
    onEditShippingAddress: () -> Unit = {},
    onChangePayment: () -> Unit = {},
    onPaymentMethodSelected: (PaymentMethod) -> Unit = {},
    onPlaceOrder: () -> Unit = {},
) {
    val scrollState = rememberScrollState()

    if (state.loadingFailed) {
        ErrorView(
            modifier = Modifier
                .fillMaxSize(),
            onRetry = onRetry
        )
    } else {
        Column(
            Modifier.fillMaxSize()
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(top = 16.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Shipping Address",
                                style = MaterialTheme.typography.subtitle1,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = state.shippingAddress?.label.orEmpty(),
                                style = MaterialTheme.typography.subtitle1,
                                fontWeight = FontWeight.Bold
                            )
                        }

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
                                    text = "Billing Address",
                                    style = MaterialTheme.typography.subtitle1
                                )
                                Spacer(modifier = Modifier.size(16.dp))
                                Text(
                                    text = state.billingAddress?.formatAddress() ?: AnnotatedString(
                                        ""
                                    ),
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
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Payment",
                            style = MaterialTheme.typography.subtitle1,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = state.selectedPaymentMethod?.title.orEmpty(),
                            style = MaterialTheme.typography.body1
                        )
                        OutlinedButton(
                            onClick = { onChangePayment() }, modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            Text("Change payment method")
                        }
                    }
                }
                Spacer(modifier = Modifier.size(16.dp))
            }
            CartTotals(
                subtotal = state.subtotalFormatted,
                total = state.totalFormatted,
                tax = state.taxFormatted,
                shippingCost = state.shippingCost,
                buttonLabel = "Place Order",
                buttonEnabled = state.isValid,
                onButtonClick = onPlaceOrder
            )
        }

        if (state.isLoading) {
            IndeterminateLoadingDialog()
        }

        if (state.isShowingPaymentMethodSelector) {
            PaymentMethodSelector(onPaymentMethodSelected = onPaymentMethodSelected)
        }
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
                email = null,
                city = "city",
                state = "state",
                postCode = "postCode",
                country = "country",
            )
        )
    )
}

@Preview
@Composable
private fun PaymentSelectionPreview() {
    CheckoutScreen(
        CheckoutViewModel.UiState(
            isShowingPaymentMethodSelector = true
        )
    )
}