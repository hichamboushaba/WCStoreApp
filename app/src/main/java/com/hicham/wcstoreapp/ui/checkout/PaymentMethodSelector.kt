package com.hicham.wcstoreapp.ui.checkout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hicham.wcstoreapp.models.PaymentMethod
import com.hicham.wcstoreapp.ui.title
import kotlinx.coroutines.launch


// TODO Check if there is a better way instead of this for showing a bottom sheet dialog
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PaymentMethodSelector(
    onPaymentMethodSelected: (PaymentMethod) -> Unit
) {

    val state = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { false }
    )

    LaunchedEffect(Unit) {
        state.show()
    }

    val scope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetContent = {
            Column {
                Text(
                    text = "Please select your payment method:",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                PaymentMethod.values().forEach {
                    Text(
                        text = it.title,
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                scope.launch {
                                    state.hide()
                                    onPaymentMethodSelected(it)
                                }
                            }
                            .padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        },
        sheetShape = MaterialTheme.shapes.medium.copy(
            bottomEnd = CornerSize(0.dp),
            bottomStart = CornerSize(0.dp)
        ),
        sheetState = state,
        content = {}
    )
}

