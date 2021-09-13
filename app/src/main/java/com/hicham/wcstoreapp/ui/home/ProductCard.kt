package com.hicham.wcstoreapp.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.ui.theme.WCStoreAppTheme

@Composable
fun ProductCard(product: Product) {
    Card() {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = product.name)
            Spacer(modifier = Modifier.size(8.dp))
            Image(
                painter = rememberImagePainter(product.images[0]),
                contentDescription = null,
                modifier = Modifier
                    .size(128.dp)
                    .padding(8.dp)
            )
        }
    }
}

@Preview
@Composable
fun DefaultCard() {
    WCStoreAppTheme {
        ProductCard(
            product = Product(
                id = 0L,
                name = "product",
                images = listOf("https://i0.wp.com/hichamwootest.wpcomstaging.com/wp-content/uploads/2020/08/logo-1.jpg?fit=800%2C799&ssl=1")
            )
        )
    }
}