package com.hicham.wcstoreapp.android.ui.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.hicham.wcstoreapp.ui.navigation.NavArgument
import com.hicham.wcstoreapp.ui.navigation.NavArgumentType

fun List<NavArgument<*>>.toNamedNavArguments(): List<NamedNavArgument> {
    return map {
        navArgument(name = it.name) {
            type = when (it.type) {
                NavArgumentType.Int -> NavType.IntType
                NavArgumentType.Long -> NavType.LongType
                NavArgumentType.Boolean -> NavType.BoolType
                NavArgumentType.String -> NavType.StringType
            }
            if (it.defaultValue != null) {
                defaultValue = it.defaultValue
            }
        }
    }
}