package com.hicham.wcstoreapp.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.ui.common.components.CategoryChip
import com.hicham.wcstoreapp.ui.products.ProductUiModel
import com.hicham.wcstoreapp.ui.products.ProductsList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    HomeScreen(
        products = viewModel.products,
        addItemToCart = viewModel::addItemToCart,
        deleteItemFromCart = viewModel::deleteItemFromCart,
        openProduct = viewModel::onProductClicked,
        scaffoldState = scaffoldState
    )
}

@Composable
private fun HomeScreen(
    products: Flow<PagingData<ProductUiModel>>,
    addItemToCart: (Product) -> Unit = {},
    deleteItemFromCart: (Product) -> Unit = {},
    openProduct: (Product) -> Unit = {},
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            CategoryChip(text = "All", isSelected = true) {

            }
            CategoryChip(text = "Clothes", isSelected = false) {

            }
            CategoryChip(text = "Clothes", isSelected = false) {

            }
            CategoryChip(text = "Clothes", isSelected = false) {

            }
        }
        ProductsList(
            productsFlow = products,
            addItemToCart = addItemToCart,
            removeItemFromCart = deleteItemFromCart,
            openProduct = openProduct,
            scaffoldState = scaffoldState
        )
    }
}

@Preview
@Composable
private fun HomePreview() {
    HomeScreen(products = emptyFlow())
}