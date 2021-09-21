package com.hicham.wcstoreapp.ui.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import compose.icons.TablerIcons
import compose.icons.tablericons.LayoutGrid
import compose.icons.tablericons.Search

sealed class Screen(
    private val baseRoute: String,
    val icon: ImageVector? = null,
    val shouldShowBottomNav: Boolean = false,
    val navArguments: List<NamedNavArgument> = emptyList()
) {
    val route: String = baseRoute.appendArguments(navArguments)

    object Home :
        Screen("home", icon = TablerIcons.LayoutGrid, shouldShowBottomNav = true)

    object Search :
        Screen("search", icon = TablerIcons.Search, shouldShowBottomNav = true)

    object Cart : Screen("cart")

    object Product : Screen(
        baseRoute = "product",
        navArguments = listOf(navArgument("productId") { type = NavType.LongType })
    ) {
        fun createRoute(productId: Long) =
            route.replace("{${navArguments.first().name}}", productId.toString())
    }
}

private fun String.appendArguments(navArguments: List<NamedNavArgument>): String {
    val mandatoryArguments = navArguments.filter { it.argument.defaultValue == null }
        .takeIf { it.isNotEmpty() }
        ?.joinToString(separator = "/", prefix = "/") { "{${it.name}}" }
        .orEmpty()
    val optionalArguments = navArguments.filter { it.argument.defaultValue != null }
        .takeIf { it.isNotEmpty() }
        ?.joinToString(separator = "&", prefix = "?") { "${it.name}={${it.name}}" }
        .orEmpty()
    return "$this$mandatoryArguments$optionalArguments"
}