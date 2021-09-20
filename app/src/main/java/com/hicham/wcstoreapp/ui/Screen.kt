package com.hicham.wcstoreapp.ui

import androidx.compose.ui.graphics.vector.ImageVector
import compose.icons.TablerIcons
import compose.icons.tablericons.LayoutGrid
import compose.icons.tablericons.Search

sealed class Screen(
    open val route: String,
    val icon: ImageVector? = null,
    val shouldShowBottomNav: Boolean = false
) {
    object Home :
        Screen("home", icon = TablerIcons.LayoutGrid, shouldShowBottomNav = true)

    object Search :
        Screen("search", icon = TablerIcons.Search, shouldShowBottomNav = true)

    object Cart : Screen("cart")

    object Product : Screen("product") {
        val productIdKey = "productId"
        override val route: String
            get() = "${super.route}/{$productIdKey}"

        fun createRoute(productId: Long) = route.replace("{$productIdKey}", productId.toString())
    }
}
