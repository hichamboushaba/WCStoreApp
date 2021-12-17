package com.hicham.wcstoreapp.ui.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.size.Scale
import com.hicham.wcstoreapp.R
import com.hicham.wcstoreapp.data.Fakes
import compose.icons.TablerIcons
import compose.icons.tablericons.Minus
import compose.icons.tablericons.Plus
import compose.icons.tablericons.Trash

@Composable
fun CartListItem(
    item: CartViewModel.CartItemUiModel,
    onIncreaseQuantity: () -> Unit,
    onDecreaseQuantity: () -> Unit,
    onRemove: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(painter = rememberImagePainter(
                data = item.product.images.firstOrNull().orEmpty(),
                builder = {
                    crossfade(true)
                    scale(Scale.FIT)
                    placeholder(R.drawable.ic_product_placeholder)
                    error(R.drawable.ic_product_placeholder)
                }
            ),
                contentDescription = "",
                modifier = Modifier
                    .size(96.dp)
                    .padding(8.dp)
                    .clip(MaterialTheme.shapes.small)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.product.name, style = MaterialTheme.typography.subtitle1)
                Spacer(modifier = Modifier.padding(2.dp))
                Text(text = item.totalPriceFormatted, style = MaterialTheme.typography.subtitle2)
                ItemQuantity(
                    quantity = item.quantity,
                    onAdd = onIncreaseQuantity,
                    onDecrease = onDecreaseQuantity
                )
            }

            IconButton(onClick = onRemove) {
                Icon(
                    imageVector = TablerIcons.Trash,
                    contentDescription = ""
                )
            }
        }
    }
}

@Composable
private fun ItemQuantity(quantity: Int, onAdd: () -> Unit, onDecrease: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onDecrease, enabled = quantity > 1) {
            Icon(
                imageVector = TablerIcons.Minus, contentDescription = "",
                Modifier.border(1.dp, Color.Gray, CircleShape)
            )
        }
        Text(text = quantity.toString())
        // TODO update enabled depending on stock
        IconButton(onClick = onAdd) {
            Icon(
                imageVector = TablerIcons.Plus, contentDescription = "",
                Modifier.border(1.dp, Color.Gray, CircleShape)
            )
        }
    }
}

@Preview
@Composable
fun CartListItemPreview() {
    val item = CartViewModel.CartItemUiModel(
        product = Fakes.product,
        totalPriceFormatted = "20 $",
        quantity = 1
    )
    CartListItem(item, {}, {}, {})
}