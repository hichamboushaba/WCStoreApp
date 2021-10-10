package com.hicham.wcstoreapp.ui.products

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberImagePainter
import coil.size.Scale
import com.hicham.wcstoreapp.R
import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.ui.home.ProductUiModel
import com.hicham.wcstoreapp.ui.theme.WCStoreAppTheme
import java.math.BigDecimal

@Composable
fun ProductCard(
    uiModel: ProductUiModel,
    addItemToCart: (Product) -> Unit,
    removeItemFromCart: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        ConstraintLayout {
            val (image, price, title, addButton, cartQuantity, minusButton) = createRefs()
            Image(
                painter = rememberImagePainter(
                    data = uiModel.product.images.firstOrNull().orEmpty(),
                    builder = {
                        crossfade(true)
                        scale(Scale.FIT)
                        placeholder(R.drawable.ic_product_placeholder)
                        error(R.drawable.ic_product_placeholder)
                    }
                ),
                contentDescription = null,
                modifier = Modifier
                    .constrainAs(image) {
                        start.linkTo(parent.start, 34.dp)
                        top.linkTo(parent.top, 8.dp)
                        bottom.linkTo(price.top, 8.dp)
                        end.linkTo(addButton.start, 2.dp)
                        width = Dimension.preferredWrapContent
                        height = Dimension.fillToConstraints
                    }
                    .wrapContentSize(Alignment.Center)
                    .clip(MaterialTheme.shapes.small)
            )
            Text(
                text = uiModel.priceFormatted,
                style = MaterialTheme.typography.caption,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                modifier = Modifier
                    .constrainAs(price) {
                        bottom.linkTo(title.top)
                        linkTo(
                            start = title.start,
                            end = addButton.start,
                            bias = 0f,
                        )
                    }
            )
            Text(
                text = uiModel.product.name, style = MaterialTheme.typography.body1,
                maxLines = 1,
                modifier = Modifier
                    .constrainAs(title) {
                        bottom.linkTo(parent.bottom)
                        linkTo(
                            start = parent.start,
                            end = addButton.start,
                            bias = 0f,
                            startMargin = 8.dp
                        )
                    }
            )

            if (uiModel.quantityInCart > 0) {
                ImageButton(
                    vectorResourceId = R.drawable.ic_minus,
                    onClicked = { removeItemFromCart(uiModel.product) },
                    modifier = Modifier
                        .constrainAs(minusButton) {
                            end.linkTo(parent.end)
                            bottom.linkTo(cartQuantity.top)
                        }
                        .clip(
                            MaterialTheme.shapes.medium.copy(
                                bottomEnd = CornerSize(0.dp),
                                bottomStart = CornerSize(0.dp),
                                topEnd = CornerSize(0.dp)
                            )
                        )
                )

                Text(
                    text = uiModel.quantityInCart.toString(),
                    color = MaterialTheme.colors.onPrimary,
                    modifier = Modifier
                        .constrainAs(cartQuantity) {
                            end.linkTo(parent.end)
                            bottom.linkTo(addButton.top)
                        }
                        .size(32.dp)
                        .background(Color.Black)
                        .wrapContentSize(Alignment.Center)
                )
            }

            ImageButton(
                vectorResourceId = R.drawable.ic_add,
                onClicked = { addItemToCart(uiModel.product) },
                modifier = Modifier.constrainAs(addButton) {
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
            )
        }
    }
}

@Composable
private fun ImageButton(
    vectorResourceId: Int,
    onClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Image(
        imageVector = ImageVector.vectorResource(id = vectorResourceId),
        contentDescription = "",
        colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary),
        modifier = modifier
            .clickable { onClicked() }
            .size(32.dp)
            .background(MaterialTheme.colors.primary)
            .padding(8.dp)
    )
}

@Preview
@Composable
fun DefaultCard() {
    WCStoreAppTheme {
        ProductCard(
            uiModel = ProductUiModel(
                product = Product(
                    id = 0L,
                    name = "product",
                    images = listOf("https://i0.wp.com/hichamwootest.wpcomstaging.com/wp-content/uploads/2020/08/logo-1.jpg?fit=800%2C799&ssl=1"),
                    price = BigDecimal.TEN,
                    shortDescription = "",
                    description = ""
                ),
                priceFormatted = "20 USD",
                quantityInCart = 1
            ),
            addItemToCart = {},
            removeItemFromCart = {},
            modifier = Modifier.size(160.dp)
        )
    }
}