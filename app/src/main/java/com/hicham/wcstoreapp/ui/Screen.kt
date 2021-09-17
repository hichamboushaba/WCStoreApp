package com.hicham.wcstoreapp.ui

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.hicham.wcstoreapp.R
import compose.icons.TablerIcons
import compose.icons.tablericons.LayoutGrid
import compose.icons.tablericons.Search

sealed class Screen(
    val route: String,
    @StringRes val resourceId: Int,
    val icon: ImageVector? = null
) {
    object Home : Screen("home", R.string.home, icon = TablerIcons.LayoutGrid)
    object Search : Screen("search", R.string.search, icon = TablerIcons.Search)
    object Cart : Screen("cart", R.string.cart)
}
