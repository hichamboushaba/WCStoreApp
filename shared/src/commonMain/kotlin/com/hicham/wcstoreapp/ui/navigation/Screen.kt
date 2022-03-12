package com.hicham.wcstoreapp.ui.navigation

sealed class Screen(
    private val baseRoute: String,
    val shouldShowBottomNav: Boolean = false,
    val navArguments: List<NavArgument<*>> = emptyList()
) {
    val route: String = baseRoute.appendArguments(navArguments)

    object Home :
        Screen("home", shouldShowBottomNav = true)

    object Search :
        Screen("search", shouldShowBottomNav = true)

    object Cart : Screen("cart")

    object Product : Screen(
        baseRoute = "product",
        navArguments = listOf(NavArgument<Long>("productId", NavArgumentType.Long))
    ) {
        fun createRoute(productId: Long) =
            route.replace("{${navArguments.first().name}}", productId.toString())
    }

    object Checkout : Screen(baseRoute = "checkout")

    object AddressList : Screen(baseRoute = "checkout/addressList")

    object AddAddress : Screen(baseRoute = "checkout/addAddress")

    object OrderPlaced : Screen(
        baseRoute = "checkout/orderplaced",
        navArguments = listOf(NavArgument<Long>("orderId", NavArgumentType.Long))
    ) {
        fun createRoute(orderId: Long) =
            route.replace("{${navArguments.first().name}}", orderId.toString())
    }
}

data class NavArgument<T>(val name: String, val type: NavArgumentType, val defaultValue: T? = null)

enum class NavArgumentType {
    Int, Long, Boolean, String
}

private fun String.appendArguments(navArguments: List<NavArgument<*>>): String {
    val mandatoryArguments = navArguments.filter { it.defaultValue == null }
        .takeIf { it.isNotEmpty() }
        ?.joinToString(separator = "/", prefix = "/") { "{${it.name}}" }
        .orEmpty()
    val optionalArguments = navArguments.filter { it.defaultValue != null }
        .takeIf { it.isNotEmpty() }
        ?.joinToString(separator = "&", prefix = "?") { "${it.name}={${it.name}}" }
        .orEmpty()
    return "$this$mandatoryArguments$optionalArguments"
}