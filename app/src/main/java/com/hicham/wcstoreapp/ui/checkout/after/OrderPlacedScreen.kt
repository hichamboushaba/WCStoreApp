package com.hicham.wcstoreapp.ui.checkout.after

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hicham.wcstoreapp.R
import compose.icons.TablerIcons

@Composable
fun OrderPlacedScreen(orderId: Long, onNavigateUp: () -> Unit) {
    val navController = rememberNavController()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Order Placed", style = MaterialTheme.typography.h4)
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.img_order_placed),
            contentDescription = ""
        )
        Text(text = "Order id is #$orderId")
        Spacer(modifier = Modifier)
        Button(onClick = onNavigateUp) {
            Text(text = "Continue shopping")
        }
    }
}

@Preview
@Composable
private fun PreviewOrderPlacedScreen() {
    OrderPlacedScreen(orderId = 20L, {})
}