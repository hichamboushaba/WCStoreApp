package com.hicham.wcstoreapp.android.ui.home

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hicham.wcstoreapp.android.ui.common.components.CategoryChip
import com.hicham.wcstoreapp.android.ui.products.ProductsList
import com.hicham.wcstoreapp.data.category.fake.FakeCategoryRepository
import com.hicham.wcstoreapp.models.Category
import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.ui.ShowSnackbar
import com.hicham.wcstoreapp.ui.home.CategoryUiModel
import com.hicham.wcstoreapp.ui.home.HomeViewModel
import com.hicham.wcstoreapp.ui.products.ProductsUiListState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    val categories by viewModel.categories.collectAsState(initial = emptyList())
    val products by viewModel.products.collectAsState(
        initial = ProductsUiListState()
    )

    LaunchedEffect("effects") {
        viewModel.effects.collect { effect ->
            when (effect) {
                is ShowSnackbar -> scaffoldState.snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    HomeScreen(
        products = products,
        categories = categories,
        onProductClicked = viewModel::onProductClicked,
        onCategorySelected = viewModel::onCategorySelected,
        loadNext = viewModel::loadNext,
        scaffoldState = scaffoldState
    )
}

@Composable
private fun HomeScreen(
    products: ProductsUiListState,
    categories: List<CategoryUiModel>,
    onProductClicked: (Product) -> Unit = {},
    onCategorySelected: (Category) -> Unit = {},
    retry: () -> Unit = {},
    loadNext: () -> Unit = {},
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(16.dp)
                .horizontalScroll(rememberScrollState())
        ) {
            categories.forEach {
                CategoryChip(text = it.category.name, isSelected = it.isSelected) {
                    onCategorySelected(it.category)
                }
            }
        }
        ProductsList(
            productsUiListState = products,
            onProductClicked = onProductClicked,
            scaffoldState = scaffoldState,
            retry = retry,
            loadNext = loadNext
        )
    }
}

@Preview
@Composable
private fun HomePreview() {
    val categories = runBlocking {
        FakeCategoryRepository().categories.map { list ->
            list.mapIndexed { index, category ->
                CategoryUiModel(
                    category,
                    isSelected = index == 0
                )
            }
        }.first()
    }
    // HomeScreen(products = emptyFlow(), categories = categories)
}