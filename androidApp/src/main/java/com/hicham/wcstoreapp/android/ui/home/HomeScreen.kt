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
import androidx.paging.PagingData
import com.hicham.wcstoreapp.android.ui.common.components.CategoryChip
import com.hicham.wcstoreapp.data.category.fake.FakeCategoryRepository
import com.hicham.wcstoreapp.models.Category
import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.ui.ShowSnackbar
import com.hicham.wcstoreapp.ui.home.CategoryUiModel
import com.hicham.wcstoreapp.ui.home.HomeViewModel
import com.hicham.wcstoreapp.ui.products.ProductUiModel
import com.hicham.wcstoreapp.ui.products.ProductsList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    val categories by viewModel.categories.collectAsState(initial = emptyList())

    LaunchedEffect("effects") {
        viewModel.effects.collect { effect ->
            when (effect) {
                is ShowSnackbar -> scaffoldState.snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    HomeScreen(
        products = viewModel.products,
        categories = categories,
        onProductClicked = viewModel::onProductClicked,
        onCategorySelected = viewModel::onCategorySelected,
        scaffoldState = scaffoldState
    )
}

@Composable
private fun HomeScreen(
    products: Flow<PagingData<ProductUiModel>>,
    categories: List<CategoryUiModel>,
    onProductClicked: (Product) -> Unit = {},
    onCategorySelected: (Category) -> Unit = {},
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
            productsFlow = products,
            onProductClicked = onProductClicked,
            scaffoldState = scaffoldState
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