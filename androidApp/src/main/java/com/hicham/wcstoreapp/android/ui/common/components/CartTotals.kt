package com.hicham.wcstoreapp.android.ui.common.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CartTotals(
    subtotal: String,
    tax: String,
    total: String,
    buttonLabel: String,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    shippingCost: String? = null,
    buttonEnabled: Boolean = true,
) {
    Surface(
        elevation = 8.dp, modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "Cart Totals", style = MaterialTheme.typography.h5)

            Spacer(modifier = Modifier.size(16.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Subtotal", style = MaterialTheme.typography.subtitle1)
                Text(
                    text = subtotal,
                    style = MaterialTheme.typography.subtitle2
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Tax", style = MaterialTheme.typography.subtitle1)
                Text(
                    text = tax,
                    style = MaterialTheme.typography.subtitle2
                )
            }
            if (shippingCost != null) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Shipping",
                        style = MaterialTheme.typography.subtitle1
                    )
                    Text(
                        text = shippingCost,
                        style = MaterialTheme.typography.subtitle2
                    )
                }
            }

            Spacer(modifier = Modifier.size(16.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Total",
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = total,
                    style = MaterialTheme.typography.subtitle2
                )
            }

            Spacer(modifier = Modifier.size(16.dp))

            Button(
                onClick = onButtonClick,
                enabled = buttonEnabled,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = buttonLabel)
            }
        }
    }
}